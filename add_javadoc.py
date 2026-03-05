import re

with open('src/main/java/com/velocityPort/Utils.java', 'r') as f:
    text = f.read()

replacements = [
    (r'public static <T> List<String> toStringList\(List<T> var0\)', 
     r'/**\n    * toStringList\n    * \n    * @param var0 La lista originale\n    * @return La lista convertita in stringhe\n    */\n   public static <T> List<String> toStringList(List<T> var0)'),
    (r'public static <T> T\[\] concat\(T\[\] var0, T\[\] var1\)',
     r'/**\n    * concat\n    * \n    * @param var0 Primo array\n    * @param var1 Secondo array\n    * @return Array concatenato\n    */\n   public static <T> T[] concat(T[] var0, T[] var1)'),
    (r'public static Component\[\] concat\(Component\[\] var0, Component\[\] var1\)',
     r'/**\n    * concat Components\n    * \n    * @param var0 Primo array di Component\n    * @param var1 Secondo array di Component\n    * @return Array concatenato di Component\n    */\n   public static Component[] concat(Component[] var0, Component[] var1)'),
    (r'public static int ordinalIndexOf\(String var0, String var1, int var2\)',
     r'/**\n    * ordinalIndexOf\n    * \n    * @param var0 Stringa originale\n    * @param var1 Sottostringa da cercare\n    * @param var2 L\'occorrenza desiderata (ennesima)\n    * @return Indice della sottostringa\n    */\n   public static int ordinalIndexOf(String var0, String var1, int var2)'),
    (r'public static String join\(String var0, String\.\.\. var1\)',
     r'/**\n    * join\n    * \n    * @param var0 Delimitatore\n    * @param var1 Elementi da unire\n    * @return Stringa unita\n    */\n   public static String join(String var0, String... var1)'),
    (r'public static String join\(String var0, Collection var1\)',
     r'/**\n    * join con Collection\n    * \n    * @param var0 Delimitatore\n    * @param var1 Collezione da unire\n    * @return Stringa unita\n    */\n   public static String join(String var0, Collection var1)'),
    (r'public static boolean containsIgnoreCase\(List<String> var0, String var1\)',
     r'/**\n    * containsIgnoreCase\n    * \n    * @param var0 Lista di stringhe\n    * @param var1 Stringa da cercare ignorando il case\n    * @return true se trovata, false altrimenti\n    */\n   public static boolean containsIgnoreCase(List<String> var0, String var1)'),
    (r'public static boolean isNameValid\(String var0\)',
     r'/**\n    * isNameValid\n    * \n    * @param var0 Nome da validare\n    * @return true se valido, false altrimenti\n    */\n   public static boolean isNameValid(String var0)'),
    (r'public static void safeConnect\(Player var0, RegisteredServer var1\)',
     r'/**\n    * safeConnect\n    * \n    * @param var0 Giocatore\n    * @param var1 Server di destinazione\n    */\n   public static void safeConnect(Player var0, RegisteredServer var1)'),
    (r'public static String formatTime\(long var0\)',
     r'/**\n    * formatTime\n    * \n    * @param var0 Timestamp in millisecondi\n    * @return Data formattata come stringa\n    */\n   public static String formatTime(long var0)'),
    (r'public static void runAsync\(Runnable r\)',
     r'/**\n    * runAsync\n    * \n    * @param r Task esplorativo da lanciare in background\n    */\n   public static void runAsync(Runnable r)'),
    (r'public static void sendMessage\(com\.velocitypowered\.api\.command\.CommandSource source, String message\)',
     r'/**\n    * sendMessage legacy\n    * \n    * @param source Destinatario\n    * @param message Messaggio testuale legacy\n    */\n   public static void sendMessage(com.velocitypowered.api.command.CommandSource source, String message)'),
    (r'public static void sendMessage\(com\.velocitypowered\.api\.command\.CommandSource source, net\.kyori\.adventure\.text\.Component message\)',
     r'/**\n    * sendMessage componente\n    * \n    * @param source Destinatario\n    * @param message Componente \n    */\n   public static void sendMessage(com.velocitypowered.api.command.CommandSource source, net.kyori.adventure.text.Component message)')
]

for pat, rep in replacements:
    text = re.sub(pat, rep, text)

with open('src/main/java/com/velocityPort/Utils.java', 'w') as f:
    f.write(text)
