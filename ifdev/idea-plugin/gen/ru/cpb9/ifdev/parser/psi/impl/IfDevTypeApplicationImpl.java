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

public class IfDevTypeApplicationImpl extends ASTWrapperPsiElement implements IfDevTypeApplication {

  public IfDevTypeApplicationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof IfDevVisitor) ((IfDevVisitor)visitor).visitTypeApplication(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public IfDevArrayTypeApplication getArrayTypeApplication() {
    return findChildByClass(IfDevArrayTypeApplication.class);
  }

  @Override
  @Nullable
  public IfDevElementId getElementId() {
    return findChildByClass(IfDevElementId.class);
  }

  @Override
  @Nullable
  public IfDevPrimitiveTypeApplication getPrimitiveTypeApplication() {
    return findChildByClass(IfDevPrimitiveTypeApplication.class);
  }

}
