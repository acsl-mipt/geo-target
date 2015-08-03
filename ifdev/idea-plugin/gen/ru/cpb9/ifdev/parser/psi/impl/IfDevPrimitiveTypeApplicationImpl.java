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

public class IfDevPrimitiveTypeApplicationImpl extends ASTWrapperPsiElement implements IfDevPrimitiveTypeApplication {

  public IfDevPrimitiveTypeApplicationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitPrimitiveTypeApplication(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public IfDevPrimitiveTypeKind getPrimitiveTypeKind() {
    return findNotNullChildByClass(IfDevPrimitiveTypeKind.class);
  }

  @Override
  @NotNull
  public PsiElement getNonNegativeNumber() {
    return findNotNullChildByType(NON_NEGATIVE_NUMBER);
  }

}
