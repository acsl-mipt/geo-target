// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface IfDevMessageParametersDecl extends PsiElement {

  @Nullable
  IfDevAllParameters getAllParameters();

  @Nullable
  IfDevDeepAllParameters getDeepAllParameters();

  @NotNull
  List<IfDevParameterDecl> getParameterDeclList();

}
