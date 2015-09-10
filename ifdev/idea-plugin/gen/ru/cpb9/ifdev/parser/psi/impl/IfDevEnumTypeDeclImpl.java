// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ru.cpb9.ifdev.parser.psi.IfDevTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ru.cpb9.ifdev.parser.psi.*;

public class IfDevEnumTypeDeclImpl extends ASTWrapperPsiElement implements IfDevEnumTypeDecl {

  public IfDevEnumTypeDeclImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitEnumTypeDecl(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public IfDevElementId getElementId() {
    return findNotNullChildByClass(IfDevElementId.class);
  }

  @Override
  @NotNull
  public IfDevEnumTypeValues getEnumTypeValues() {
    return findNotNullChildByClass(IfDevEnumTypeValues.class);
  }

  @Override
  @Nullable
  public IfDevInfoString getInfoString() {
    return findChildByClass(IfDevInfoString.class);
  }

}
