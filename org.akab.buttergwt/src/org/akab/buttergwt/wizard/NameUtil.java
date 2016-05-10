package org.akab.buttergwt.wizard;

import java.beans.Introspector;

public class NameUtil
{
  public static String toTitleCase(String str)
  {
    if ((str == null) || (str.length() == 0)) {
      return str;
    }
    StringBuffer result = new StringBuffer();
    
    char prevChar = ' ';
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      if (c == '_')
      {
        result.append(' ');
      }
      else if ((prevChar == ' ') || (prevChar == '_'))
      {
        result.append(Character.toUpperCase(c));
      }
      else if ((Character.isUpperCase(c)) && (!Character.isUpperCase(prevChar)))
      {
        result.append(' ');
        result.append(Character.toUpperCase(c));
      }
      else
      {
        result.append(c);
      }
      prevChar = c;
    }
    String resultString = result.toString().replaceAll(" ", "_").toUpperCase();
    return resultString;
  }
  
  public static String toLowerCaseFirstLetter(String source)
  {
    return Introspector.decapitalize(source);
  }
}
