package com.asahiflair.ahomkeyboard.utils;


import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;

public class Transliterator {

    public static void main(String[] args) {
        System.out.println(romanToAhom("s"));
        System.out.println(convertToUnicode(romanToAhom("mau,")));
    }

    public static String convertToAhom(String s){
        try {
            return convertToUnicode(romanToAhom(s));
        }catch (Exception e) {
            return "";
        }
    }

    public static String convertToRoman(String s){
        try {
            return ahomToRoman(convertToFont(s));
        }catch (Exception e) {
            return "error"+s;
        }
    }
    public static String romanToAhom(String s) {
        // replace b with B
        String consonants = "kx[csNtvnpfmyrlbhAdBG";
        String x = s.replaceAll("[^àāáâèēéêìīíîòōóôùūúûa-zA-Z0-9 ]", ""); // remove alphanumeric chars
        x = x.toLowerCase();
        x = x.replaceAll("kh", "x");
        x = x.replaceAll("th", "v");
        x = x.replaceAll("ng", "[");
        x = x.replaceAll("ny", "N");
        x = x.replaceAll("ph", "f");
        x = x.replaceAll("bh", "B");
        x = x.replaceAll("dh", "G");
        x = x.replaceAll("zh", "c");
        x = x.replaceAll("ts", "c");
        x = x.replaceAll("ch", "c");

        // set of two-letter consonants
        Set<String> tlc = new HashSet<>(Arrays.asList("kh", "th", "ng", "ny",
                "ph","bh","dh","zh", "ts", "ch"));

        if (x.length()==1)
            return x;
        else if (x.length()==2 && tlc.contains(x))
            return x;

        LinkedHashMap<String, String> vowels = new LinkedHashMap<>();
        vowels.put("aao", ",w");
        vowels.put("aai", ",j");
        vowels.put("aeu", "Vw");
        vowels.put("ao", "w");
        vowels.put("ae", "e]");
        vowels.put("aa", "a");
        vowels.put("aw", "ea");
        vowels.put("au", "]");
        vowels.put("ai", "j");
        vowels.put("oi", "oj");
        vowels.put("ui", "uNq");
        vowels.put("uu", "U");
        vowels.put("iu", "iw");
        vowels.put("eu", "Cw");
        vowels.put("ei", "ENq");
        vowels.put("ee", "I");
        vowels.put("er", "E");
        vowels.put("u", "u");
        vowels.put("e", "e");
        vowels.put("o", "Uw");
        vowels.put("i", "i");
        vowels.put("raa", "Sa");
        vowels.put("ru", "Su");
        vowels.put("ruu", "SU");
        vowels.put("ri", "Si");
        vowels.put("ree", "SI");
        vowels.put("laa", "Ya");
        vowels.put("lu", "Yu");
        vowels.put("luu", "YU");
        vowels.put("li", "Yi");
        vowels.put("lee", "YI");

        String[] words = x.split(" ");
        ArrayList<String> result = new ArrayList<>();
        for (String y : words) {
            String w = "";
            String xWord = y;
            // replace words starting from w with b  (wan to ban etc)
            if (xWord.charAt(0) == 'w') {
                xWord = 'b' + xWord.substring(1);
            }

            for (String key : vowels.keySet()) {
                // at, am , etc
                if (xWord.charAt(0) == 'a' && consonants.indexOf(xWord.charAt(1)) != -1) {
                    if (y.charAt(1) == 'y') {
                        result.add("AN");
                    } else {
                        result.add("A" + xWord.charAt(1) + "q");
                    }
                    break;
                } else if (xWord.equals(key) || xWord.equals("a" + key)) {  // if word is vowel
                    if (xWord.substring(xWord.length() - 2).equals("er")) { // aer
                        result.add("AEw");
                    } else {
                        result.add("A" + vowels.get(key)); // ee, aw, aao etc
                    }
                    break;
                } else if ((xWord.startsWith(key) || (xWord.charAt(0) == 'a' && xWord.substring(1).startsWith(key))) && consonants.indexOf(xWord.charAt(xWord.length() - 1)) != -1) { // et, awt
                    // special logic for o
                    result.add("A" + shortVowels(key.equals("o") ? "U" : vowels.get(key)) + xWord.charAt(xWord.length() - 1) + "q");
                    break;
                } else if (consonants.indexOf(xWord.charAt(0)) != -1 && xWord.substring(1).startsWith(key)) {
                    if (xWord.length() == key.length() + 1) {
                        if (xWord.substring(xWord.length() - 2).equals("er")) { // ser, mer, etc
                            result.add(xWord.charAt(0) + "Ew");
                        } else {
                            result.add(xWord.charAt(0) + vowels.get(key)); // kaa, kaao, etc
                        }
                        break;
                    } else if (xWord.length() == key.length() + 2 && consonants.indexOf(xWord.charAt(xWord.length() - 1)) != -1) { // lik, khit, etc
                        result.add(xWord.charAt(0) + shortVowels(key.equals("o") ? "U" : vowels.get(key)) + xWord.charAt(xWord.length() - 1) + "q" );
                        break;
                    }
                } else if (consonants.indexOf(xWord.charAt(0)) != -1 && xWord.length() == 3 && xWord.charAt(1) == 'a' && consonants.indexOf(xWord.charAt(2)) != -1) {
                    result.add(xWord.charAt(0) + "" + xWord.charAt(2) + "q"); // a m k etc
                    break;
                } else if ((consonants.indexOf(xWord.charAt(0)) != -1 && xWord.charAt(1) == 'a' && xWord.length() == 2) || (consonants.indexOf(xWord.charAt(0)) != -1 && xWord.length() == 1)) {
                    result.add(xWord.charAt(0) + ""); // a m k etc
                    break;
                } else if (isNumeric(xWord)) {
                    result.add(xWord); // 123, 45, etc
                    break;
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i).replaceAll("(.)(e)", "e$1")); // bring e backward
            result.set(i, result.get(i).replaceAll("(.)(S)", "S$1")); // bring S backward
        }
        if (!s.isEmpty()) {
            System.out.println(s);
            char lastChar = s.charAt(s.length() - 1);
            if (lastChar ==',')
                lastChar='!';
            else if (lastChar ==';')
                lastChar = '@';
            else if (lastChar ==':')
                lastChar = '#';
            else if (lastChar =='.')
                lastChar='$';
            else
                lastChar='~'; //skip
            if (lastChar!='~')
                return String.join(" ", result) + lastChar;
        }
        return String.join(" ", result);
    }

    public static String ahomToRoman(String x) {
        x = x.replaceAll("(e)(.)", "$2e"); //bring e backward
        String consonants = "kx[csNtvnpfmyrlbhAdBG";
        LinkedHashMap <String, String> vowels = new LinkedHashMap<>();
        vowels.put(",w", "aao");
        vowels.put(",M", "aam");
        vowels.put("oM", "awm");
        vowels.put(",j", "aai");
        vowels.put("Vw", "aeu");
        vowels.put("Uw", "o");
        vowels.put("Ew", "er");
        vowels.put("E", "er");
        vowels.put("w", "ao");
        vowels.put("ea", "aw");
        vowels.put("e]", "ae");
        vowels.put("a", "aa");
        vowels.put("o", "aw");
        vowels.put("]", "au");
        vowels.put("j", "ai");
        vowels.put("oj", "oi");
        vowels.put("uNq", "ui");
        vowels.put("iw", "iu");
        vowels.put("Cw", "eu");
        vowels.put("ENq", "ei");
        vowels.put("I", "ee");
        vowels.put("u", "u");
        vowels.put("e", "e");
        vowels.put("i", "i");
        vowels.put("Sa", "raa");
        vowels.put("Su", "ru");
        vowels.put("SU", "ruu");
        vowels.put("Si", "ri");
        vowels.put("SI", "ree");
        vowels.put("S", "ra");
        vowels.put("Ya", "laa");
        vowels.put("Yu", "lu");
        vowels.put("YU", "luu");
        vowels.put("Yi", "li");
        vowels.put("YI", "lee");
        vowels.put("Y", "la");
        vowels.put(",", "aa");
        vowels.put("C", "e");
        vowels.put("V", "ae");
        vowels.put("M", "am");

        String[] words = x.split(" ");
        ArrayList<String> result = new ArrayList<>();
        for (String y : words) {
            String temp = y;

            if (temp.length() > 1 && consonants.contains(String.valueOf(temp.charAt(0))) && consonants.contains(String.valueOf(temp.charAt(1)))) {
                temp = temp.charAt(0) + "%" + temp.substring(1); // rk -> rak etc
            }
            temp = temp.replace("q", "");
            temp = temp.replace("x", "kh");
            temp = temp.replace("v", "th");
            temp = temp.replace("[", "ng");
            temp = temp.replace("N", "ny");
            temp = temp.replace("f", "ph");
            temp = temp.replace("B", "bh");
            temp = temp.replace("G", "dh");
            temp = temp.replace("c", "ts");

            if (temp.contains("%")) {
                temp = temp.replace("%", addTone(temp, "a")); // rak, lak, etc tone
            } else {
                for (String key : vowels.keySet()) {
                    if (temp.contains(key)) {
                        temp = temp.replace(key, addTone(temp, vowels.get(key)));
                        break;
                    }
                }
            }

            // special logic for U
            if (temp.charAt(temp.length() - 1) == 'U' || (temp.length() > 1 && temp.charAt(temp.length() - 2) == 'U' && "!@#$".contains(String.valueOf(temp.charAt(temp.length() - 1))))) {
                temp = temp.replace("U", addTone(temp, "uu")); // kuu
            } else {
                temp = temp.replace("U", addTone(temp, "o")); // kon
            }

            temp = temp.replaceAll("([!@#$])", ""); //remove tones
            temp = temp.replace("b", "w"); // b to w
            temp = temp.replace("wh", "bh");
            temp = temp.replace("M", "m"); // M to m

            if (temp.charAt(0) == 'A' && temp.length() > 1) {
                temp = temp.substring(1);
            }

            result.add(temp);
        }

        //replace all non latin characters
        return String.join(" ", result).replaceAll("[^\\p{IsLatin}\\p{IsCommon}\\p{IsInherited}]", "");
    }

    public static String addTone(String x, String vowel) {
        List<List<String>> tones = List.of(
                List.of("à", "è", "ì", "ò", "ù"),
                List.of("ā", "ē", "ī", "ō", "ū"),
                List.of("á", "é", "í", "ó", "ú"),
                List.of("â", "ê", "î", "ô", "û")
        );

        Map<Character, Integer> ind = new LinkedHashMap<>() {{
            put('a', 0);
            put('e', 1);
            put('i', 2);
            put('o', 3);
            put('u', 4);
        }};

        Map<Character, Integer> t = new LinkedHashMap<>() {{
            put('!', 0);
            put('@', 1);
            put('#', 2);
            put('$', 3);
        }};

        if ("!@#$".contains(String.valueOf(x.charAt(x.length() - 1)))) { // has tones
            if (!"aeiou".contains(String.valueOf(vowel.charAt(0)))) { // -raa, -klaa etc
                return vowel.charAt(0) + tones.get(t.get(x.charAt(x.length() - 1))).get(ind.get(vowel.charAt(1))) + vowel.substring(2);
            } else {
                return tones.get(t.get(x.charAt(x.length() - 1))).get(ind.get(vowel.charAt(0))) + vowel.substring(1);
            }
        }
        return vowel;
    }
    public static String convertToUnicode(String x) {
        x = x.replaceAll("(e)(.)", "$2e"); //bring e afterwards
        x = x.replaceAll("(S)(.)", "$2S"); //bring ra-glide afterwards
        x = x.replaceAll("oj", "jo"); //oi
        x = x.replaceAll("oM", "Mo"); //om
        x = x.replaceAll("uM", "Mu"); //um
        x = x.replaceAll("UM", "MU"); //uum
        x = x.replaceAll(",j", "j,"); //aai

        Map<String, String> ahom = new LinkedHashMap<>();
        ahom.put("k", "𑜀");
        ahom.put("x", "𑜁");
        ahom.put("n", "𑜃");
        ahom.put("[", "𑜂");
        ahom.put("t", "𑜄");
        ahom.put("p", "𑜆");
        ahom.put("f", "𑜇");
        ahom.put("b", "𑜈");
        ahom.put("m", "𑜉");
        ahom.put("y", "𑜊");
        ahom.put("c", "𑜋");
        ahom.put("v", "𑜌");
        ahom.put("r", "𑜍");
        ahom.put("l", "𑜎");
        ahom.put("s", "𑜏");
        ahom.put("N", "𑜐");
        ahom.put("h", "𑜑");
        ahom.put("A", "𑜒");
        ahom.put("d", "𑜓");
        ahom.put("K", "𑜕");
        ahom.put("g", "𑜕");
        ahom.put("G", "𑜗");
        ahom.put("Q", "𑜙");
        ahom.put("D", "𑜔");
        ahom.put("B", "𑜘");
        ahom.put("S", "𑜞");
        ahom.put("Y", "𑜝");
        ahom.put("a", "𑜡");
        ahom.put(",", "า");
        ahom.put("i", "𑜢");
        ahom.put("I", "𑜣");
        ahom.put("u", "𑜤");
        ahom.put("U", "𑜥");
        ahom.put("e", "𑜦");
        ahom.put("]", "𑜧");
        ahom.put("w", "𑜈𑜫");
        ahom.put("E", "𑜢𑜤");
        ahom.put("V", "𑜦𑜦");
        ahom.put("C", "𑜦");
        ahom.put("o", "𑜨");
        ahom.put("j", "𑜩");
        ahom.put("M", "𑜉𑜫");
        ahom.put("q", "𑜫");
        ahom.put("@", ";");
        ahom.put("#", ":");
        ahom.put("$", ".");
        ahom.put("%", "ะ");

        for (Map.Entry<String, String> entry : ahom.entrySet()) {
            x = x.replace(entry.getKey(), entry.getValue());
        }
        x = x.replaceAll("!",",");

        return x;
    }

    public static String convertToFont(String x) {
        Map<String, String> ahom = new LinkedHashMap<>();
        ahom.put("𑜢𑜤", "E");
        ahom.put("𑜚𑜫", "w");
        ahom.put("𑜈𑜫", "w");
        ahom.put("𑜀", "k");
        ahom.put("𑜁", "x");
        ahom.put("𑜃", "n");
        ahom.put("𑜂", "[");
        ahom.put("𑜄", "t");
        ahom.put("𑜆", "p");
        ahom.put("𑜇", "f");
        ahom.put("𑜈", "b");
        ahom.put("𑜉", "m");
        ahom.put("𑜊", "y");
        ahom.put("𑜋", "c");
        ahom.put("𑜌", "v");
        ahom.put("𑜍", "r");
        ahom.put("𑜎", "l");
        ahom.put("𑜏", "s");
        ahom.put("𑜐", "N");
        ahom.put("𑜑", "h");
        ahom.put("𑜒", "A");
        ahom.put("𑜓", "d");
        ahom.put("𑜕", "K");
        ahom.put("𑜗", "G");
        ahom.put("𑜙", "Q");
        ahom.put("𑜔", "D");
        ahom.put("𑜘", "B");
        ahom.put("𑜞", "S");
        ahom.put("𑜝", "Y");
        ahom.put(",", "!");
        ahom.put(";", "@");
        ahom.put(":", "#");
        ahom.put(".", "$");
        ahom.put("ะ", "%");
        ahom.put("𑜡", "a");
        ahom.put("า", ",");
        ahom.put("𑜢", "i");
        ahom.put("𑜣", "I");
        ahom.put("𑜤", "u");
        ahom.put("𑜥", "U");
        ahom.put("𑜦𑜦", "V");
        ahom.put("𑜦", "e");
        ahom.put("𑜧", "]");
        ahom.put("𑜨", "o");
        ahom.put("𑜩", "j");
        ahom.put("𑜪", "M");
        ahom.put("𑜫", "q");

        for (Map.Entry<String, String> entry : ahom.entrySet()) {
            x = x.replace(entry.getKey(), entry.getValue());
        }

        x = x.replaceAll("(e)(.)(q)", "C$2q");
        x = x.replaceAll("(.)(e)", "e$1");
        x = x.replaceAll("jo", "oj");
        x = x.replaceAll("j,", ",j");
        x = x.replaceAll("Mo", "oM");
        x = x.replaceAll("Mu", "uM");
        x = x.replaceAll("MU", "UM");

        return x;
    }
    private static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    public static String shortVowels(String x) {
        if (x.equals("a")) {
            return ",";
        } else if (x.equals("e]")) {
            return "V";
        } else if (x.equals("e")) {
            return "C";
        } else if (x.equals("ea")) {
            return "o";
        } else {
            return x;
        }
    }
}

