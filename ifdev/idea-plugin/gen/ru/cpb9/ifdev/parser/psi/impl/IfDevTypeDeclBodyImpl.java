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

public class IfDevTypeDeclBodyImpl extends ASTWrapperPsiElement implements IfDevTypeDeclBody {

  public IfDevTypeDeclBodyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitTypeDeclBody(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public IfDevEnumTypeDecl getEnumTypeDecl() {
    return findChildByClass(IfDevEnumTypeDecl.class);
  }

  @Override
  @Nullable
  public IfDevInfoString getInfoString() {
    return findChildByClass(IfDevInfoString.class);
  }

  @Override
  @Nullable
  public IfDevStructTypeDecl getStructTypeDecl() {
    return findChildByClass(IfDevStructTypeDecl.class);
  }

  @Override
  @Nullable
  public IfDevTypeApplication getTypeApplication() {
    return findChildByClass(IfDevTypeApplication.class);
  }

}
