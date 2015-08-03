// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface IfDevMessageDecl extends PsiElement {

  @Nullable
  IfDevDynamicStatusMessage getDynamicStatusMessage();

  @NotNull
  IfDevElementNameRule getElementNameRule();

  @Nullable
  IfDevEventMessage getEventMessage();

  @Nullable
  IfDevInfoString getInfoString();

  @Nullable
  IfDevStatusMessage getStatusMessage();

  @NotNull
  PsiElement getNonNegativeNumber();

}
