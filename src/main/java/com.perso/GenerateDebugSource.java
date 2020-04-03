package com.perso;

import com.intellij.CommonBundle;
import com.intellij.compiler.impl.FileSetCompileScope;
import com.intellij.debugger.actions.ViewAsGroup;
import com.intellij.debugger.engine.JavaValue;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.lang.UrlClassLoader;
import com.intellij.util.xmlb.XmlSerializer;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateDebugSource extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // Using the event, implement an action. For example, create and show a dialog.
        // Using the event, create and show a dialog
        final Project project = getEventProject(event);
        JavaValue jvv = ViewAsGroup.getSelectedValues(event).get(0);
        Value value = jvv.getDescriptor().getValue();

        String className = value.type().name();
        final VirtualFile[] virtualFiles = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        final VirtualFile virtualFile = virtualFiles[0];

        final Module module = ModuleUtilCore.findModuleForFile(virtualFile, project);
        if (module == null || virtualFile == null) return;

        CompilerManager.getInstance(project).make(new FileSetCompileScope(Collections.singletonList(virtualFile), new Module[]{module}), new CompileStatusNotification() {
            @Override
            public void finished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
                if (aborted || errors > 0) return;
            }
        });
        final List<URL> urls = new ArrayList<>();
        final List<String> list = OrderEnumerator.orderEntries(module).recursively().runtimeOnly().getPathsList().getPathList();
        for (String path : list) {
            try {
                urls.add(new File(FileUtil.toSystemIndependentName(path)).toURI().toURL());
            }
            catch (MalformedURLException e1) {
            }
        }

        UrlClassLoader loader = UrlClassLoader.build().urls(urls).parent(XmlSerializer.class.getClassLoader()).get();
        final Class<?> aClass;
        try {
            CodeGenerator codeGenerator = new CodeGenerator();
            aClass = Class.forName(className, true, loader);
            if (value instanceof ObjectReference) {
                Object debugObject = codeGenerator.createObjectFromReference(aClass, (ObjectReference) value, loader);
                String msg = codeGenerator.generateCode(debugObject);
                Messages.showMessageDialog(msg, "YOO", Messages.getInformationIcon());
            }
        }
        catch (ClassNotFoundException e) {
            Messages.showErrorDialog(project, "Cannot find class '" + className + "'", CommonBundle.getErrorTitle());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
        }
    }

    @Nullable
    private static PsiElement getPsiClass(AnActionEvent e) {
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null || psiFile == null) return null;
        final PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        return element;
    }

}
