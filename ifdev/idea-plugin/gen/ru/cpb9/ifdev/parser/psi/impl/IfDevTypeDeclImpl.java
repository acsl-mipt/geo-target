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

public class IfDevTypeDeclImpl extends ASTWrapperPsiElement implements IfDevTypeDecl {

  public IfDevTypeDeclImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitTypeDecl(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public IfDevElementNameRule getElementNameRule() {
    return findNotNullChildByClass(IfDevElementNameRule.class);
  }

  @Override
  @Nullable
  public IfDevInfoString getInfoString() {
    return findChildByClass(IfDevInfoString.class);
  }

  @Override
  @NotNull
  public IfDevTypeDeclBody getTypeDeclBody() {
    return findNotNullChildByClass(IfDevTypeDeclBody.class);
  }

}