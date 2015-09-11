// This is a generated file. Not intended for manual editing.
package ru.cpb9.ifdev.parser.idea;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static ru.cpb9.ifdev.parser.psi.IfDevTypes.*;
import static ru.cpb9.ifdev.parser.idea.IfDevParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class IfDevParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ALIAS_DECL) {
      r = alias_decl(b, 0);
    }
    else if (t == ARRAY_TYPE_APPLICATION) {
      r = array_type_application(b, 0);
    }
    else if (t == COMMAND_ARG) {
      r = command_arg(b, 0);
    }
    else if (t == COMMAND_ARGS) {
      r = command_args(b, 0);
    }
    else if (t == COMMAND_DECL) {
      r = command_decl(b, 0);
    }
    else if (t == COMPONENT_BASE_TYPE_DECL) {
      r = component_base_type_decl(b, 0);
    }
    else if (t == COMPONENT_DECL) {
      r = component_decl(b, 0);
    }
    else if (t == DYNAMIC_STATUS_MESSAGE) {
      r = dynamic_status_message(b, 0);
    }
    else if (t == ELEMENT_ID) {
      r = element_id(b, 0);
    }
    else if (t == ELEMENT_NAME_RULE) {
      r = element_name_rule(b, 0);
    }
    else if (t == ENUM_TYPE_DECL) {
      r = enum_type_decl(b, 0);
    }
    else if (t == ENUM_TYPE_VALUE) {
      r = enum_type_value(b, 0);
    }
    else if (t == ENUM_TYPE_VALUES) {
      r = enum_type_values(b, 0);
    }
    else if (t == EVENT_MESSAGE) {
      r = event_message(b, 0);
    }
    else if (t == FLOAT_LITERAL) {
      r = float_literal(b, 0);
    }
    else if (t == INFO_STRING) {
      r = info_string(b, 0);
    }
    else if (t == LENGTH_FROM) {
      r = length_from(b, 0);
    }
    else if (t == LENGTH_TO) {
      r = length_to(b, 0);
    }
    else if (t == LITERAL) {
      r = literal(b, 0);
    }
    else if (t == MESSAGE_DECL) {
      r = message_decl(b, 0);
    }
    else if (t == MESSAGE_PARAMETERS_DECL) {
      r = message_parameters_decl(b, 0);
    }
    else if (t == NAMESPACE_DECL) {
      r = namespace_decl(b, 0);
    }
    else if (t == NATIVE_TYPE_KIND) {
      r = native_type_kind(b, 0);
    }
    else if (t == PARAMETER_DECL) {
      r = parameter_decl(b, 0);
    }
    else if (t == PARAMETER_ELEMENT) {
      r = parameter_element(b, 0);
    }
    else if (t == PRIMITIVE_TYPE_APPLICATION) {
      r = primitive_type_application(b, 0);
    }
    else if (t == PRIMITIVE_TYPE_KIND) {
      r = primitive_type_kind(b, 0);
    }
    else if (t == STATUS_MESSAGE) {
      r = status_message(b, 0);
    }
    else if (t == STRING_VALUE) {
      r = string_value(b, 0);
    }
    else if (t == STRUCT_TYPE_DECL) {
      r = struct_type_decl(b, 0);
    }
    else if (t == SUBCOMPONENT_DECL) {
      r = subcomponent_decl(b, 0);
    }
    else if (t == TYPE_APPLICATION) {
      r = type_application(b, 0);
    }
    else if (t == TYPE_DECL) {
      r = type_decl(b, 0);
    }
    else if (t == TYPE_DECL_BODY) {
      r = type_decl_body(b, 0);
    }
    else if (t == TYPE_UNIT_APPLICATION) {
      r = type_unit_application(b, 0);
    }
    else if (t == UNIT) {
      r = unit(b, 0);
    }
    else if (t == UNIT_DECL) {
      r = unit_decl(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return ifDevFile(b, l + 1);
  }

  /* ********************************************************** */
  // ALIAS element_id type_application info_string?
  public static boolean alias_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alias_decl")) return false;
    if (!nextTokenIs(b, ALIAS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ALIAS);
    r = r && element_id(b, l + 1);
    r = r && type_application(b, l + 1);
    r = r && alias_decl_3(b, l + 1);
    exit_section_(b, m, ALIAS_DECL, r);
    return r;
  }

  // info_string?
  private static boolean alias_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alias_decl_3")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LEFT_BRACKET type_application (COMMA length_from (DOTS (length_to | STAR))?)? RIGHT_BRACKET
  public static boolean array_type_application(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_application")) return false;
    if (!nextTokenIs(b, LEFT_BRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_BRACKET);
    r = r && type_application(b, l + 1);
    r = r && array_type_application_2(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACKET);
    exit_section_(b, m, ARRAY_TYPE_APPLICATION, r);
    return r;
  }

  // (COMMA length_from (DOTS (length_to | STAR))?)?
  private static boolean array_type_application_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_application_2")) return false;
    array_type_application_2_0(b, l + 1);
    return true;
  }

  // COMMA length_from (DOTS (length_to | STAR))?
  private static boolean array_type_application_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_application_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && length_from(b, l + 1);
    r = r && array_type_application_2_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOTS (length_to | STAR))?
  private static boolean array_type_application_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_application_2_0_2")) return false;
    array_type_application_2_0_2_0(b, l + 1);
    return true;
  }

  // DOTS (length_to | STAR)
  private static boolean array_type_application_2_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_application_2_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOTS);
    r = r && array_type_application_2_0_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // length_to | STAR
  private static boolean array_type_application_2_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_application_2_0_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = length_to(b, l + 1);
    if (!r) r = consumeToken(b, STAR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type_unit_application element_name_rule info_string?
  public static boolean command_arg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_arg")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<command arg>");
    r = type_unit_application(b, l + 1);
    r = r && element_name_rule(b, l + 1);
    r = r && command_arg_2(b, l + 1);
    exit_section_(b, l, m, COMMAND_ARG, r, false, null);
    return r;
  }

  // info_string?
  private static boolean command_arg_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_arg_2")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // command_arg (COMMA command_arg)* COMMA?
  public static boolean command_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_args")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<command args>");
    r = command_arg(b, l + 1);
    r = r && command_args_1(b, l + 1);
    r = r && command_args_2(b, l + 1);
    exit_section_(b, l, m, COMMAND_ARGS, r, false, null);
    return r;
  }

  // (COMMA command_arg)*
  private static boolean command_args_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_args_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!command_args_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "command_args_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // COMMA command_arg
  private static boolean command_args_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_args_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && command_arg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA?
  private static boolean command_args_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_args_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // COMMAND element_name_rule COLON NON_NEGATIVE_NUMBER info_string? LEFT_PAREN command_args? RIGHT_PAREN
  public static boolean command_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_decl")) return false;
    if (!nextTokenIs(b, COMMAND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMAND);
    r = r && element_name_rule(b, l + 1);
    r = r && consumeTokens(b, 0, COLON, NON_NEGATIVE_NUMBER);
    r = r && command_decl_4(b, l + 1);
    r = r && consumeToken(b, LEFT_PAREN);
    r = r && command_decl_6(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    exit_section_(b, m, COMMAND_DECL, r);
    return r;
  }

  // info_string?
  private static boolean command_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_decl_4")) return false;
    info_string(b, l + 1);
    return true;
  }

  // command_args?
  private static boolean command_decl_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_decl_6")) return false;
    command_args(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_decl_body
  public static boolean component_base_type_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_base_type_decl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<component base type decl>");
    r = type_decl_body(b, l + 1);
    exit_section_(b, l, m, COMPONENT_BASE_TYPE_DECL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // COMPONENT element_name_rule (COLON NON_NEGATIVE_NUMBER)? (WITH subcomponent_decl (COMMA subcomponent_decl)*)? info_string?
  //     LEFT_BRACE component_base_type_decl? (command_decl|message_decl)* RIGHT_BRACE
  public static boolean component_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl")) return false;
    if (!nextTokenIs(b, COMPONENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMPONENT);
    r = r && element_name_rule(b, l + 1);
    r = r && component_decl_2(b, l + 1);
    r = r && component_decl_3(b, l + 1);
    r = r && component_decl_4(b, l + 1);
    r = r && consumeToken(b, LEFT_BRACE);
    r = r && component_decl_6(b, l + 1);
    r = r && component_decl_7(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACE);
    exit_section_(b, m, COMPONENT_DECL, r);
    return r;
  }

  // (COLON NON_NEGATIVE_NUMBER)?
  private static boolean component_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_2")) return false;
    component_decl_2_0(b, l + 1);
    return true;
  }

  // COLON NON_NEGATIVE_NUMBER
  private static boolean component_decl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COLON, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, null, r);
    return r;
  }

  // (WITH subcomponent_decl (COMMA subcomponent_decl)*)?
  private static boolean component_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_3")) return false;
    component_decl_3_0(b, l + 1);
    return true;
  }

  // WITH subcomponent_decl (COMMA subcomponent_decl)*
  private static boolean component_decl_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WITH);
    r = r && subcomponent_decl(b, l + 1);
    r = r && component_decl_3_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA subcomponent_decl)*
  private static boolean component_decl_3_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_3_0_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!component_decl_3_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "component_decl_3_0_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // COMMA subcomponent_decl
  private static boolean component_decl_3_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_3_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && subcomponent_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // info_string?
  private static boolean component_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_4")) return false;
    info_string(b, l + 1);
    return true;
  }

  // component_base_type_decl?
  private static boolean component_decl_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_6")) return false;
    component_base_type_decl(b, l + 1);
    return true;
  }

  // (command_decl|message_decl)*
  private static boolean component_decl_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_7")) return false;
    int c = current_position_(b);
    while (true) {
      if (!component_decl_7_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "component_decl_7", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // command_decl|message_decl
  private static boolean component_decl_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "component_decl_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_decl(b, l + 1);
    if (!r) r = message_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DYNAMIC STATUS message_parameters_decl
  public static boolean dynamic_status_message(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dynamic_status_message")) return false;
    if (!nextTokenIs(b, DYNAMIC)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DYNAMIC, STATUS);
    r = r && message_parameters_decl(b, l + 1);
    exit_section_(b, m, DYNAMIC_STATUS_MESSAGE, r);
    return r;
  }

  /* ********************************************************** */
  // element_name_rule (DOT element_name_rule)*
  public static boolean element_id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_id")) return false;
    if (!nextTokenIs(b, "<element id>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<element id>");
    r = element_name_rule(b, l + 1);
    r = r && element_id_1(b, l + 1);
    exit_section_(b, l, m, ELEMENT_ID, r, false, null);
    return r;
  }

  // (DOT element_name_rule)*
  private static boolean element_id_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_id_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!element_id_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "element_id_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // DOT element_name_rule
  private static boolean element_id_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_id_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && element_name_rule(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ELEMENT_NAME_TOKEN | ESCAPED_NAME
  public static boolean element_name_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_name_rule")) return false;
    if (!nextTokenIs(b, "<element name rule>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<element name rule>");
    r = consumeToken(b, ELEMENT_NAME_TOKEN);
    if (!r) r = consumeToken(b, ESCAPED_NAME);
    exit_section_(b, l, m, ELEMENT_NAME_RULE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ENUM element_id info_string? LEFT_PAREN enum_type_values RIGHT_PAREN
  public static boolean enum_type_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_decl")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ENUM);
    r = r && element_id(b, l + 1);
    r = r && enum_type_decl_2(b, l + 1);
    r = r && consumeToken(b, LEFT_PAREN);
    r = r && enum_type_values(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    exit_section_(b, m, ENUM_TYPE_DECL, r);
    return r;
  }

  // info_string?
  private static boolean enum_type_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_decl_2")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // element_name_rule EQ_SIGN literal info_string?
  public static boolean enum_type_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_value")) return false;
    if (!nextTokenIs(b, "<enum type value>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<enum type value>");
    r = element_name_rule(b, l + 1);
    r = r && consumeToken(b, EQ_SIGN);
    r = r && literal(b, l + 1);
    r = r && enum_type_value_3(b, l + 1);
    exit_section_(b, l, m, ENUM_TYPE_VALUE, r, false, null);
    return r;
  }

  // info_string?
  private static boolean enum_type_value_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_value_3")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // enum_type_value (COMMA enum_type_value)* COMMA?
  public static boolean enum_type_values(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_values")) return false;
    if (!nextTokenIs(b, "<enum type values>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<enum type values>");
    r = enum_type_value(b, l + 1);
    r = r && enum_type_values_1(b, l + 1);
    r = r && enum_type_values_2(b, l + 1);
    exit_section_(b, l, m, ENUM_TYPE_VALUES, r, false, null);
    return r;
  }

  // (COMMA enum_type_value)*
  private static boolean enum_type_values_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_values_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!enum_type_values_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "enum_type_values_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // COMMA enum_type_value
  private static boolean enum_type_values_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_values_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && enum_type_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA?
  private static boolean enum_type_values_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_type_values_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // EVENT message_parameters_decl
  public static boolean event_message(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "event_message")) return false;
    if (!nextTokenIs(b, EVENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EVENT);
    r = r && message_parameters_decl(b, l + 1);
    exit_section_(b, m, EVENT_MESSAGE, r);
    return r;
  }

  /* ********************************************************** */
  // (PLUS | MINUS)? (NON_NEGATIVE_NUMBER DOT NON_NEGATIVE_NUMBER? | DOT NON_NEGATIVE_NUMBER) (("e" | "E") (PLUS | MINUS)? NON_NEGATIVE_NUMBER)?
  public static boolean float_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<float literal>");
    r = float_literal_0(b, l + 1);
    r = r && float_literal_1(b, l + 1);
    r = r && float_literal_2(b, l + 1);
    exit_section_(b, l, m, FLOAT_LITERAL, r, false, null);
    return r;
  }

  // (PLUS | MINUS)?
  private static boolean float_literal_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_0")) return false;
    float_literal_0_0(b, l + 1);
    return true;
  }

  // PLUS | MINUS
  private static boolean float_literal_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    exit_section_(b, m, null, r);
    return r;
  }

  // NON_NEGATIVE_NUMBER DOT NON_NEGATIVE_NUMBER? | DOT NON_NEGATIVE_NUMBER
  private static boolean float_literal_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = float_literal_1_0(b, l + 1);
    if (!r) r = parseTokens(b, 0, DOT, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, null, r);
    return r;
  }

  // NON_NEGATIVE_NUMBER DOT NON_NEGATIVE_NUMBER?
  private static boolean float_literal_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, NON_NEGATIVE_NUMBER, DOT);
    r = r && float_literal_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NON_NEGATIVE_NUMBER?
  private static boolean float_literal_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_1_0_2")) return false;
    consumeToken(b, NON_NEGATIVE_NUMBER);
    return true;
  }

  // (("e" | "E") (PLUS | MINUS)? NON_NEGATIVE_NUMBER)?
  private static boolean float_literal_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_2")) return false;
    float_literal_2_0(b, l + 1);
    return true;
  }

  // ("e" | "E") (PLUS | MINUS)? NON_NEGATIVE_NUMBER
  private static boolean float_literal_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = float_literal_2_0_0(b, l + 1);
    r = r && float_literal_2_0_1(b, l + 1);
    r = r && consumeToken(b, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, null, r);
    return r;
  }

  // "e" | "E"
  private static boolean float_literal_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "e");
    if (!r) r = consumeToken(b, "E");
    exit_section_(b, m, null, r);
    return r;
  }

  // (PLUS | MINUS)?
  private static boolean float_literal_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_2_0_1")) return false;
    float_literal_2_0_1_0(b, l + 1);
    return true;
  }

  // PLUS | MINUS
  private static boolean float_literal_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // namespace_decl (component_decl | unit_decl | type_decl | alias_decl)*
  static boolean ifDevFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifDevFile")) return false;
    if (!nextTokenIs(b, NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespace_decl(b, l + 1);
    r = r && ifDevFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (component_decl | unit_decl | type_decl | alias_decl)*
  private static boolean ifDevFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifDevFile_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!ifDevFile_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ifDevFile_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // component_decl | unit_decl | type_decl | alias_decl
  private static boolean ifDevFile_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifDevFile_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = component_decl(b, l + 1);
    if (!r) r = unit_decl(b, l + 1);
    if (!r) r = type_decl(b, l + 1);
    if (!r) r = alias_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // INFO string_value
  public static boolean info_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "info_string")) return false;
    if (!nextTokenIs(b, INFO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INFO);
    r = r && string_value(b, l + 1);
    exit_section_(b, m, INFO_STRING, r);
    return r;
  }

  /* ********************************************************** */
  // NON_NEGATIVE_NUMBER
  public static boolean length_from(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "length_from")) return false;
    if (!nextTokenIs(b, NON_NEGATIVE_NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, LENGTH_FROM, r);
    return r;
  }

  /* ********************************************************** */
  // NON_NEGATIVE_NUMBER
  public static boolean length_to(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "length_to")) return false;
    if (!nextTokenIs(b, NON_NEGATIVE_NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, LENGTH_TO, r);
    return r;
  }

  /* ********************************************************** */
  // float_literal | NON_NEGATIVE_NUMBER | TRUE | FALSE
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<literal>");
    r = float_literal(b, l + 1);
    if (!r) r = consumeToken(b, NON_NEGATIVE_NUMBER);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    exit_section_(b, l, m, LITERAL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MESSAGE element_name_rule COLON NON_NEGATIVE_NUMBER info_string? (status_message | event_message | dynamic_status_message)
  public static boolean message_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_decl")) return false;
    if (!nextTokenIs(b, MESSAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MESSAGE);
    r = r && element_name_rule(b, l + 1);
    r = r && consumeTokens(b, 0, COLON, NON_NEGATIVE_NUMBER);
    r = r && message_decl_4(b, l + 1);
    r = r && message_decl_5(b, l + 1);
    exit_section_(b, m, MESSAGE_DECL, r);
    return r;
  }

  // info_string?
  private static boolean message_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_decl_4")) return false;
    info_string(b, l + 1);
    return true;
  }

  // status_message | event_message | dynamic_status_message
  private static boolean message_decl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_decl_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = status_message(b, l + 1);
    if (!r) r = event_message(b, l + 1);
    if (!r) r = dynamic_status_message(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LEFT_PAREN parameter_decl (COMMA parameter_decl)* COMMA? RIGHT_PAREN
  public static boolean message_parameters_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_parameters_decl")) return false;
    if (!nextTokenIs(b, LEFT_PAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_PAREN);
    r = r && parameter_decl(b, l + 1);
    r = r && message_parameters_decl_2(b, l + 1);
    r = r && message_parameters_decl_3(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    exit_section_(b, m, MESSAGE_PARAMETERS_DECL, r);
    return r;
  }

  // (COMMA parameter_decl)*
  private static boolean message_parameters_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_parameters_decl_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!message_parameters_decl_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "message_parameters_decl_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // COMMA parameter_decl
  private static boolean message_parameters_decl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_parameters_decl_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && parameter_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA?
  private static boolean message_parameters_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "message_parameters_decl_3")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // NAMESPACE element_id
  public static boolean namespace_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_decl")) return false;
    if (!nextTokenIs(b, NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAMESPACE);
    r = r && element_id(b, l + 1);
    exit_section_(b, m, NAMESPACE_DECL, r);
    return r;
  }

  /* ********************************************************** */
  // BER
  public static boolean native_type_kind(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "native_type_kind")) return false;
    if (!nextTokenIs(b, BER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BER);
    exit_section_(b, m, NATIVE_TYPE_KIND, r);
    return r;
  }

  /* ********************************************************** */
  // parameter_element info_string?
  public static boolean parameter_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_decl")) return false;
    if (!nextTokenIs(b, "<parameter decl>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<parameter decl>");
    r = parameter_element(b, l + 1);
    r = r && parameter_decl_1(b, l + 1);
    exit_section_(b, l, m, PARAMETER_DECL, r, false, null);
    return r;
  }

  // info_string?
  private static boolean parameter_decl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_decl_1")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // element_id ((LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)+
  //     (DOT element_id (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)?)*)?
  public static boolean parameter_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element")) return false;
    if (!nextTokenIs(b, "<parameter element>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<parameter element>");
    r = element_id(b, l + 1);
    r = r && parameter_element_1(b, l + 1);
    exit_section_(b, l, m, PARAMETER_ELEMENT, r, false, null);
    return r;
  }

  // ((LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)+
  //     (DOT element_id (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)?)*)?
  private static boolean parameter_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1")) return false;
    parameter_element_1_0(b, l + 1);
    return true;
  }

  // (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)+
  //     (DOT element_id (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)?)*
  private static boolean parameter_element_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter_element_1_0_0(b, l + 1);
    r = r && parameter_element_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)+
  private static boolean parameter_element_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter_element_1_0_0_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!parameter_element_1_0_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_element_1_0_0", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET
  private static boolean parameter_element_1_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LEFT_BRACKET, NON_NEGATIVE_NUMBER);
    r = r && parameter_element_1_0_0_0_2(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOTS NON_NEGATIVE_NUMBER)?
  private static boolean parameter_element_1_0_0_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_0_0_2")) return false;
    parameter_element_1_0_0_0_2_0(b, l + 1);
    return true;
  }

  // DOTS NON_NEGATIVE_NUMBER
  private static boolean parameter_element_1_0_0_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_0_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOTS, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOT element_id (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)?)*
  private static boolean parameter_element_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!parameter_element_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_element_1_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // DOT element_id (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)?
  private static boolean parameter_element_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && element_id(b, l + 1);
    r = r && parameter_element_1_0_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET)?
  private static boolean parameter_element_1_0_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_1_0_2")) return false;
    parameter_element_1_0_1_0_2_0(b, l + 1);
    return true;
  }

  // LEFT_BRACKET NON_NEGATIVE_NUMBER (DOTS NON_NEGATIVE_NUMBER)? RIGHT_BRACKET
  private static boolean parameter_element_1_0_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_1_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LEFT_BRACKET, NON_NEGATIVE_NUMBER);
    r = r && parameter_element_1_0_1_0_2_0_2(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOTS NON_NEGATIVE_NUMBER)?
  private static boolean parameter_element_1_0_1_0_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_1_0_2_0_2")) return false;
    parameter_element_1_0_1_0_2_0_2_0(b, l + 1);
    return true;
  }

  // DOTS NON_NEGATIVE_NUMBER
  private static boolean parameter_element_1_0_1_0_2_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_element_1_0_1_0_2_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOTS, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // primitive_type_kind COLON NON_NEGATIVE_NUMBER | native_type_kind
  public static boolean primitive_type_application(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive_type_application")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<primitive type application>");
    r = primitive_type_application_0(b, l + 1);
    if (!r) r = native_type_kind(b, l + 1);
    exit_section_(b, l, m, PRIMITIVE_TYPE_APPLICATION, r, false, null);
    return r;
  }

  // primitive_type_kind COLON NON_NEGATIVE_NUMBER
  private static boolean primitive_type_application_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive_type_application_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = primitive_type_kind(b, l + 1);
    r = r && consumeTokens(b, 0, COLON, NON_NEGATIVE_NUMBER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UINT | INT | FLOAT | BOOL
  public static boolean primitive_type_kind(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive_type_kind")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<primitive type kind>");
    r = consumeToken(b, UINT);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, BOOL);
    exit_section_(b, l, m, PRIMITIVE_TYPE_KIND, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STATUS message_parameters_decl
  public static boolean status_message(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "status_message")) return false;
    if (!nextTokenIs(b, STATUS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STATUS);
    r = r && message_parameters_decl(b, l + 1);
    exit_section_(b, m, STATUS_MESSAGE, r);
    return r;
  }

  /* ********************************************************** */
  // STRING | STRING_UNARY_QUOTES
  public static boolean string_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_value")) return false;
    if (!nextTokenIs(b, "<string value>", STRING, STRING_UNARY_QUOTES)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<string value>");
    r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, STRING_UNARY_QUOTES);
    exit_section_(b, l, m, STRING_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STRUCT info_string? LEFT_PAREN command_arg (COMMA command_arg)* COMMA ? RIGHT_PAREN
  public static boolean struct_type_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_decl")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRUCT);
    r = r && struct_type_decl_1(b, l + 1);
    r = r && consumeToken(b, LEFT_PAREN);
    r = r && command_arg(b, l + 1);
    r = r && struct_type_decl_4(b, l + 1);
    r = r && struct_type_decl_5(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    exit_section_(b, m, STRUCT_TYPE_DECL, r);
    return r;
  }

  // info_string?
  private static boolean struct_type_decl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_decl_1")) return false;
    info_string(b, l + 1);
    return true;
  }

  // (COMMA command_arg)*
  private static boolean struct_type_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_decl_4")) return false;
    int c = current_position_(b);
    while (true) {
      if (!struct_type_decl_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "struct_type_decl_4", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // COMMA command_arg
  private static boolean struct_type_decl_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_decl_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && command_arg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA ?
  private static boolean struct_type_decl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_decl_5")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // element_name_rule
  public static boolean subcomponent_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subcomponent_decl")) return false;
    if (!nextTokenIs(b, "<subcomponent decl>", ELEMENT_NAME_TOKEN, ESCAPED_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<subcomponent decl>");
    r = element_name_rule(b, l + 1);
    exit_section_(b, l, m, SUBCOMPONENT_DECL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // element_id | primitive_type_application | array_type_application
  public static boolean type_application(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_application")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<type application>");
    r = element_id(b, l + 1);
    if (!r) r = primitive_type_application(b, l + 1);
    if (!r) r = array_type_application(b, l + 1);
    exit_section_(b, l, m, TYPE_APPLICATION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TYPE_KEYWORD element_name_rule info_string? type_decl_body
  public static boolean type_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl")) return false;
    if (!nextTokenIs(b, TYPE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TYPE_KEYWORD);
    r = r && element_name_rule(b, l + 1);
    r = r && type_decl_2(b, l + 1);
    r = r && type_decl_body(b, l + 1);
    exit_section_(b, m, TYPE_DECL, r);
    return r;
  }

  // info_string?
  private static boolean type_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl_2")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (type_application info_string?) | enum_type_decl | struct_type_decl
  public static boolean type_decl_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<type decl body>");
    r = type_decl_body_0(b, l + 1);
    if (!r) r = enum_type_decl(b, l + 1);
    if (!r) r = struct_type_decl(b, l + 1);
    exit_section_(b, l, m, TYPE_DECL_BODY, r, false, null);
    return r;
  }

  // type_application info_string?
  private static boolean type_decl_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_application(b, l + 1);
    r = r && type_decl_body_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // info_string?
  private static boolean type_decl_body_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl_body_0_1")) return false;
    info_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_application unit?
  public static boolean type_unit_application(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_unit_application")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<type unit application>");
    r = type_application(b, l + 1);
    r = r && type_unit_application_1(b, l + 1);
    exit_section_(b, l, m, TYPE_UNIT_APPLICATION, r, false, null);
    return r;
  }

  // unit?
  private static boolean type_unit_application_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_unit_application_1")) return false;
    unit(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SLASH element_id SLASH
  public static boolean unit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit")) return false;
    if (!nextTokenIs(b, SLASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SLASH);
    r = r && element_id(b, l + 1);
    r = r && consumeToken(b, SLASH);
    exit_section_(b, m, UNIT, r);
    return r;
  }

  /* ********************************************************** */
  // UNIT_TOKEN element_name_rule (DISPLAY string_value)? (PLACEMENT (BEFORE | AFTER))? info_string?
  public static boolean unit_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl")) return false;
    if (!nextTokenIs(b, UNIT_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNIT_TOKEN);
    r = r && element_name_rule(b, l + 1);
    r = r && unit_decl_2(b, l + 1);
    r = r && unit_decl_3(b, l + 1);
    r = r && unit_decl_4(b, l + 1);
    exit_section_(b, m, UNIT_DECL, r);
    return r;
  }

  // (DISPLAY string_value)?
  private static boolean unit_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl_2")) return false;
    unit_decl_2_0(b, l + 1);
    return true;
  }

  // DISPLAY string_value
  private static boolean unit_decl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DISPLAY);
    r = r && string_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (PLACEMENT (BEFORE | AFTER))?
  private static boolean unit_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl_3")) return false;
    unit_decl_3_0(b, l + 1);
    return true;
  }

  // PLACEMENT (BEFORE | AFTER)
  private static boolean unit_decl_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PLACEMENT);
    r = r && unit_decl_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BEFORE | AFTER
  private static boolean unit_decl_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl_3_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BEFORE);
    if (!r) r = consumeToken(b, AFTER);
    exit_section_(b, m, null, r);
    return r;
  }

  // info_string?
  private static boolean unit_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_decl_4")) return false;
    info_string(b, l + 1);
    return true;
  }

}