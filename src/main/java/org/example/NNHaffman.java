package org.example;

import java.util.*;

public class NNHaffman {
    public static void main(String[] args) {

        String text =
                "Sri Lanka Cricket wishes to emphasise that " +
                        "it adopts a 'zero tolerance' policy for any such conduct " +
                        "by a player and will provide all the required support to " +
                        "the Australian law enforcement authorities to carry out an " +
                        "impartial inquiry into the incident.";

        long time = System.nanoTime();

        // Делаем TreeMap символ: количество повторений символа в тексте (Реализация такая-же как и в задаче Хафмана)
        TreeMap<Character, Integer> frequencies = countFrequency(text);
        printTreeMap(countFrequency(text));

        // Создаем HashMap символ в качестве ключа и бинарный код для кодировки в качестве значения(код сгенерирован как будто-бы у нас было сбалансированное дерево)
        Map<Character, String> codes = createCodesMap(frequencies);

        System.out.println("Prefix code table: " + codes);
        System.out.println("**************************************************************************");

        //Кодируем нашу строку бинарным кодом (Реализация такая-же как и в задаче Хафмана)
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            encoded.append(codes.get(text.charAt(i)));
        }

        System.out.println("Source string size:" + text.getBytes().length * 8 + " bit.");
        System.out.println("Compressed string size:" + encoded.length() + " bit.");
        System.out.println("Compressed string bits: " + encoded);

        //А как раскодировать?
        Map<String, Character> codesRevers = codesRevers(codes);
        String decoded = makeDecode(encoded.toString(), codesRevers);

        System.out.println("Decoded string:" + decoded);

        System.out.println("NanoTime: " + (System.nanoTime() - time));
    }

    // Считаем сколько раз символ встретился в тексте
    private static TreeMap<Character, Integer> countFrequency(String text) {
        TreeMap<Character, Integer> freqMap = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            Integer count = freqMap.get(c);
            freqMap.put(c, count != null ? count + 1 : 1);
        }
        return freqMap;
    }

    // Делаем HashMap символ и его код
    private static Map<Character, String> createCodesMap(TreeMap<Character, Integer> frequencies) {

        Map<Character, String> codes = new HashMap<>();
        int i = 0;
        int lengthCode = Integer.toString(frequencies.size(), 2).toCharArray().length;
        for (Character c : frequencies.keySet()) {
            codes.put(c, createOneCode(i++, lengthCode));
        }
        return codes;
    }


    // Генерируем бинарные ключи для кодировки символа, в этом случае все ключи будут одной длины
    // не зависимо от количества повторений буквы в тексте
    private static String createOneCode(int i, int lengthCode) {
        StringBuilder code = new StringBuilder();
        String binaryNumber = Integer.toString(i, 2);
        while (code.length() < lengthCode - binaryNumber.length()) {
            code.append('0');
        }
        code.append(binaryNumber);
        return code.toString();
    }


    private static Map<String, Character> codesRevers(Map<Character, String> codes) {
        Map<String, Character> reverseMap = new HashMap<>();

        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            reverseMap.put(entry.getValue(), entry.getKey());
        }
        return reverseMap;
    }

    private static String makeDecode(String encoded, Map<String, Character> codes) {

        //Нам нужен StringBuilder для хранения полученной строки (расшифрованных данных).
        StringBuilder decoded = new StringBuilder();

        StringBuilder key = new StringBuilder();

        //Теперь проходимся по нашим БИТАМ, нашей зашифрованной строки.
        for (int i = 0; i < encoded.length(); i++) {
            if (codes.containsKey(key.toString())) {
                decoded.append(codes.get(key.toString()));
                key = new StringBuilder();

            }
            key.append(encoded.charAt(i));
        }
        decoded.append(codes.get(key.toString()));
        return decoded.toString();
    }


    private static void printTreeMap(TreeMap<Character, Integer> freqMap) {
        Iterator iterator = freqMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry ent = (Map.Entry) iterator.next();
            String key = ent.getKey().toString();
            String value = ent.getValue().toString();
            System.out.print("(" + key + ")-" + value + "/ ");
        }
        System.out.println();
    }

}
