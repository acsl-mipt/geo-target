// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface IfDevCommandDecl extends PsiElement {

  @Nullable
  IfDevCommandArgs getCommandArgs();

  @NotNull
  IfDevElementNameRule getElementNameRule();

  @Nullable
  IfDevInfoString getInfoString();

  @NotNull
  PsiElement getNonNegativeNumber();

}