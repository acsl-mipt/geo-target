// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import ru.cpb9.ifdev.parser.psi.impl.*;

public interface IfDevTypes {

  IElementType ALIAS_DECL = new IfDevElementType("ALIAS_DECL");
  IElementType ARRAY_TYPE_APPLICATION = new IfDevElementType("ARRAY_TYPE_APPLICATION");
  IElementType COMMAND_ARG = new IfDevElementType("COMMAND_ARG");
  IElementType COMMAND_ARGS = new IfDevElementType("COMMAND_ARGS");
  IElementType COMMAND_DECL = new IfDevElementType("COMMAND_DECL");
  IElementType COMPONENT_BASE_TYPE_DECL = new IfDevElementType("COMPONENT_BASE_TYPE_DECL");
  IElementType COMPONENT_DECL = new IfDevElementType("COMPONENT_DECL");
  IElementType DYNAMIC_STATUS_MESSAGE = new IfDevElementType("DYNAMIC_STATUS_MESSAGE");
  IElementType ELEMENT_ID = new IfDevElementType("ELEMENT_ID");
  IElementType ELEMENT_NAME_RULE = new IfDevElementType("ELEMENT_NAME_RULE");
  IElementType ENUM_TYPE_DECL = new IfDevElementType("ENUM_TYPE_DECL");
  IElementType ENUM_TYPE_VALUE = new IfDevElementType("ENUM_TYPE_VALUE");
  IElementType ENUM_TYPE_VALUES = new IfDevElementType("ENUM_TYPE_VALUES");
  IElementType EVENT_MESSAGE = new IfDevElementType("EVENT_MESSAGE");
  IElementType FLOAT_LITERAL = new IfDevElementType("FLOAT_LITERAL");
  IElementType INFO_STRING = new IfDevElementType("INFO_STRING");
  IElementType LENGTH_FROM = new IfDevElementType("LENGTH_FROM");
  IElementType LENGTH_TO = new IfDevElementType("LENGTH_TO");
  IElementType LITERAL = new IfDevElementType("LITERAL");
  IElementType MESSAGE_DECL = new IfDevElementType("MESSAGE_DECL");
  IElementType MESSAGE_PARAMETERS_DECL = new IfDevElementType("MESSAGE_PARAMETERS_DECL");
  IElementType NAMESPACE_DECL = new IfDevElementType("NAMESPACE_DECL");
  IElementType NATIVE_TYPE_KIND = new IfDevElementType("NATIVE_TYPE_KIND");
  IElementType PARAMETER_DECL = new IfDevElementType("PARAMETER_DECL");
  IElementType PARAMETER_ELEMENT = new IfDevElementType("PARAMETER_ELEMENT");
  IElementType PRIMITIVE_TYPE_APPLICATION = new IfDevElementType("PRIMITIVE_TYPE_APPLICATION");
  IElementType PRIMITIVE_TYPE_KIND = new IfDevElementType("PRIMITIVE_TYPE_KIND");
  IElementType STATUS_MESSAGE = new IfDevElementType("STATUS_MESSAGE");
  IElementType STRING_VALUE = new IfDevElementType("STRING_VALUE");
  IElementType STRUCT_TYPE_DECL = new IfDevElementType("STRUCT_TYPE_DECL");
  IElementType SUBCOMPONENT_DECL = new IfDevElementType("SUBCOMPONENT_DECL");
  IElementType TYPE_APPLICATION = new IfDevElementType("TYPE_APPLICATION");
  IElementType TYPE_DECL = new IfDevElementType("TYPE_DECL");
  IElementType TYPE_DECL_BODY = new IfDevElementType("TYPE_DECL_BODY");
  IElementType TYPE_UNIT_APPLICATION = new IfDevElementType("TYPE_UNIT_APPLICATION");
  IElementType UNIT = new IfDevElementType("UNIT");
  IElementType UNIT_DECL = new IfDevElementType("UNIT_DECL");

  IElementType AFTER = new IfDevTokenType("after");
  IElementType ALIAS = new IfDevTokenType("alias");
  IElementType ARRAY = new IfDevTokenType("array");
  IElementType BASE_TYPE = new IfDevTokenType("base_type");
  IElementType BEFORE = new IfDevTokenType("before");
  IElementType BER = new IfDevTokenType("ber");
  IElementType BOOL = new IfDevTokenType("bool");
  IElementType COLON = new IfDevTokenType(":");
  IElementType COMMA = new IfDevTokenType(",");
  IElementType COMMAND = new IfDevTokenType("command");
  IElementType COMMENT = new IfDevTokenType("COMMENT");
  IElementType COMPONENT = new IfDevTokenType("component");
  IElementType DISPLAY = new IfDevTokenType("display");
  IElementType DOT = new IfDevTokenType(".");
  IElementType DOTS = new IfDevTokenType("..");
  IElementType DYNAMIC = new IfDevTokenType("dynamic");
  IElementType ELEMENT_NAME_TOKEN = new IfDevTokenType("ELEMENT_NAME_TOKEN");
  IElementType ENUM = new IfDevTokenType("enum");
  IElementType EQ_SIGN = new IfDevTokenType("=");
  IElementType ESCAPED_NAME = new IfDevTokenType("ESCAPED_NAME");
  IElementType EVENT = new IfDevTokenType("event");
  IElementType FALSE = new IfDevTokenType("false");
  IElementType FLOAT = new IfDevTokenType("float");
  IElementType INFO = new IfDevTokenType("info");
  IElementType INT = new IfDevTokenType("int");
  IElementType LEFT_BRACE = new IfDevTokenType("{");
  IElementType LEFT_BRACKET = new IfDevTokenType("[");
  IElementType LEFT_PAREN = new IfDevTokenType("(");
  IElementType MESSAGE = new IfDevTokenType("message");
  IElementType MINUS = new IfDevTokenType("-");
  IElementType MULTILINE_COMMENT = new IfDevTokenType("MULTILINE_COMMENT");
  IElementType NAMESPACE = new IfDevTokenType("namespace");
  IElementType NON_NEGATIVE_NUMBER = new IfDevTokenType("NON_NEGATIVE_NUMBER");
  IElementType PARAMETER = new IfDevTokenType("parameter");
  IElementType PLACEMENT = new IfDevTokenType("placement");
  IElementType PLUS = new IfDevTokenType("+");
  IElementType RIGHT_BRACE = new IfDevTokenType("}");
  IElementType RIGHT_BRACKET = new IfDevTokenType("]");
  IElementType RIGHT_PAREN = new IfDevTokenType(")");
  IElementType SLASH = new IfDevTokenType("/");
  IElementType STAR = new IfDevTokenType("*");
  IElementType STATUS = new IfDevTokenType("status");
  IElementType STRING = new IfDevTokenType("STRING");
  IElementType STRING_UNARY_QUOTES = new IfDevTokenType("STRING_UNARY_QUOTES");
  IElementType STRUCT = new IfDevTokenType("struct");
  IElementType SUBCOMPONENT = new IfDevTokenType("subcomponent");
  IElementType TRUE = new IfDevTokenType("true");
  IElementType TYPE_KEYWORD = new IfDevTokenType("type");
  IElementType UINT = new IfDevTokenType("uint");
  IElementType UNIT_TOKEN = new IfDevTokenType("unit");
  IElementType WITH = new IfDevTokenType("with");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ALIAS_DECL) {
        return new IfDevAliasDeclImpl(node);
      }
      else if (type == ARRAY_TYPE_APPLICATION) {
        return new IfDevArrayTypeApplicationImpl(node);
      }
      else if (type == COMMAND_ARG) {
        return new IfDevCommandArgImpl(node);
      }
      else if (type == COMMAND_ARGS) {
        return new IfDevCommandArgsImpl(node);
      }
      else if (type == COMMAND_DECL) {
        return new IfDevCommandDeclImpl(node);
      }
      else if (type == COMPONENT_BASE_TYPE_DECL) {
        return new IfDevComponentBaseTypeDeclImpl(node);
      }
      else if (type == COMPONENT_DECL) {
        return new IfDevComponentDeclImpl(node);
      }
      else if (type == DYNAMIC_STATUS_MESSAGE) {
        return new IfDevDynamicStatusMessageImpl(node);
      }
      else if (type == ELEMENT_ID) {
        return new IfDevElementIdImpl(node);
      }
      else if (type == ELEMENT_NAME_RULE) {
        return new IfDevElementNameRuleImpl(node);
      }
      else if (type == ENUM_TYPE_DECL) {
        return new IfDevEnumTypeDeclImpl(node);
      }
      else if (type == ENUM_TYPE_VALUE) {
        return new IfDevEnumTypeValueImpl(node);
      }
      else if (type == ENUM_TYPE_VALUES) {
        return new IfDevEnumTypeValuesImpl(node);
      }
      else if (type == EVENT_MESSAGE) {
        return new IfDevEventMessageImpl(node);
      }
      else if (type == FLOAT_LITERAL) {
        return new IfDevFloatLiteralImpl(node);
      }
      else if (type == INFO_STRING) {
        return new IfDevInfoStringImpl(node);
      }
      else if (type == LENGTH_FROM) {
        return new IfDevLengthFromImpl(node);
      }
      else if (type == LENGTH_TO) {
        return new IfDevLengthToImpl(node);
      }
      else if (type == LITERAL) {
        return new IfDevLiteralImpl(node);
      }
      else if (type == MESSAGE_DECL) {
        return new IfDevMessageDeclImpl(node);
      }
      else if (type == MESSAGE_PARAMETERS_DECL) {
        return new IfDevMessageParametersDeclImpl(node);
      }
      else if (type == NAMESPACE_DECL) {
        return new IfDevNamespaceDeclImpl(node);
      }
      else if (type == NATIVE_TYPE_KIND) {
        return new IfDevNativeTypeKindImpl(node);
      }
      else if (type == PARAMETER_DECL) {
        return new IfDevParameterDeclImpl(node);
      }
      else if (type == PARAMETER_ELEMENT) {
        return new IfDevParameterElementImpl(node);
      }
      else if (type == PRIMITIVE_TYPE_APPLICATION) {
        return new IfDevPrimitiveTypeApplicationImpl(node);
      }
      else if (type == PRIMITIVE_TYPE_KIND) {
        return new IfDevPrimitiveTypeKindImpl(node);
      }
      else if (type == STATUS_MESSAGE) {
        return new IfDevStatusMessageImpl(node);
      }
      else if (type == STRING_VALUE) {
        return new IfDevStringValueImpl(node);
      }
      else if (type == STRUCT_TYPE_DECL) {
        return new IfDevStructTypeDeclImpl(node);
      }
      else if (type == SUBCOMPONENT_DECL) {
        return new IfDevSubcomponentDeclImpl(node);
      }
      else if (type == TYPE_APPLICATION) {
        return new IfDevTypeApplicationImpl(node);
      }
      else if (type == TYPE_DECL) {
        return new IfDevTypeDeclImpl(node);
      }
      else if (type == TYPE_DECL_BODY) {
        return new IfDevTypeDeclBodyImpl(node);
      }
      else if (type == TYPE_UNIT_APPLICATION) {
        return new IfDevTypeUnitApplicationImpl(node);
      }
      else if (type == UNIT) {
        return new IfDevUnitImpl(node);
      }
      else if (type == UNIT_DECL) {
        return new IfDevUnitDeclImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
