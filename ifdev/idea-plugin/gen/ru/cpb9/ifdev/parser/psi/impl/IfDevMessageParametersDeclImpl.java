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

public class IfDevMessageParametersDeclImpl extends ASTWrapperPsiElement implements IfDevMessageParametersDecl {

  public IfDevMessageParametersDeclImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitMessageParametersDecl(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public IfDevAllParameters getAllParameters() {
    return findChildByClass(IfDevAllParameters.class);
  }

  @Override
  @Nullable
  public IfDevDeepAllParameters getDeepAllParameters() {
    return findChildByClass(IfDevDeepAllParameters.class);
  }

  @Override
  @NotNull
  public List<IfDevParameterDecl> getParameterDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, IfDevParameterDecl.class);
  }

}
