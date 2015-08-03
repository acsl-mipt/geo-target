// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface IfDevComponentDecl extends PsiElement {

  @NotNull
  List<IfDevCommandDecl> getCommandDeclList();

  @Nullable
  IfDevComponentBaseTypeDecl getComponentBaseTypeDecl();

  @NotNull
  IfDevElementNameRule getElementNameRule();

  @Nullable
  IfDevInfoString getInfoString();

  @NotNull
  List<IfDevMessageDecl> getMessageDeclList();

  @NotNull
  List<IfDevSubcomponentDecl> getSubcomponentDeclList();

}
