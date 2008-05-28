/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.ideaplugin;

import com.intellij.CommonBundle;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * NewClassAction
 *
 * @author Brian Goetz
 */
public class NewFxClassAction extends CreateElementActionBase {

    public NewFxClassAction() {
        super("JavaFX Class", "Creates JavaFX Class", FxPlugin.FX_ICON);
    }

    @NotNull
    protected final PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
        Module module = ModuleUtil.findModuleForFile(directory.getVirtualFile(), project);
        if (module == null)
            return PsiElement.EMPTY_ARRAY;

        MyInputValidator validator = new MyInputValidator(project, directory);
        Messages.showInputDialog(project, "Enter a new class name", "New JavaFX Class",
                Messages.getQuestionIcon(), "", validator);

        return validator.getCreatedElements();
    }

    @NotNull
    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        // Eventually, get this from a template
        final PsiManager psiManager = PsiManager.getInstance(directory.getProject());
        final PsiFile file = psiManager.getElementFactory().createFileFromText(newName + "." + FxPlugin.FX_FILE_EXTENSION, "");
        CodeStyleManager.getInstance(psiManager).reformat(file);
        return new PsiElement[]{(PsiFile) directory.add(file)};
    }

    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }

    protected final void checkBeforeCreate(String newName, PsiDirectory directory) throws IncorrectOperationException {
        directory.checkCreateClass(newName);
    }

    protected String getCommandName() {
        return "Create JavaFX Class";
    }

    protected String getActionName(PsiDirectory psiDirectory, String s) {
        return "Creating JavaFX Class " + s;
    }
}
