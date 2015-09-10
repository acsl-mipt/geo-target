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

public class IfDevComponentDeclImpl extends ASTWrapperPsiElement implements IfDevComponentDecl {

  public IfDevComponentDeclImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitComponentDecl(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<IfDevCommandDecl> getCommandDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, IfDevCommandDecl.class);
  }

  @Override
  @Nullable
  public IfDevComponentBaseTypeDecl getComponentBaseTypeDecl() {
    return findChildByClass(IfDevComponentBaseTypeDecl.class);
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
  public List<IfDevMessageDecl> getMessageDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, IfDevMessageDecl.class);
  }

  @Override
  @NotNull
  public List<IfDevSubcomponentDecl> getSubcomponentDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, IfDevSubcomponentDecl.class);
  }

  @Override
  @Nullable
  public PsiElement getNonNegativeNumber() {
    return findChildByType(NON_NEGATIVE_NUMBER);
  }

}
