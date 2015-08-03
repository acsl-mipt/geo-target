package ru.cpb9.ifdev.idea.plugin;

import ru.cpb9.ifdev.parser.IfDevLanguage;
import ru.cpb9.ifdev.parser.psi.IfDevTypes;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevCompletionContributor extends CompletionContributor
{
    public IfDevCompletionContributor()
    {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(IfDevTypes.ELEMENT_NAME_RULE).withLanguage(IfDevLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>()
                {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                                  @NotNull CompletionResultSet result)
                    {
//                        PsiActionSupportFactory.PsiElementSelector
                        System.out.println(parameters.getPosition().getParent().getNode());
                        result.addElement(LookupElementBuilder.create("balalaika"));
                    }
                });
    }
}
