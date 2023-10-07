package org.example;

public class S_DES {
    public static String key1;
    public static String key2;

    //转换装置设定
    //1.密钥拓展装置
    // P10 置换
    public static int[] P10 = new int[]{3, 5, 2, 7, 4, 10, 1, 9, 8, 6};

    // P8 置换
    public static int[] P8 = new int[]{6, 3, 7, 4, 8, 5, 10, 9};


    //2.初始置换盒
    // 初始置换IP
    public static int[] IP = new int[]{2, 6, 3, 1, 4, 8, 5, 7};
    // 最终置换盒
    public static int[] IP_1 = new int[]{4, 1, 3, 5, 7, 2, 8, 6};

    //3.轮函数F
    // 扩展置换EP
    public static int[] EP = new int[]{4, 1, 2, 3, 2, 3, 4, 1};
    // S1 盒
    public static String[][] S1_box = new String[][]{
            {"01", "00", "11", "10"},
            {"11", "10", "01", "00"},
            {"00", "10", "01", "11"},
            {"11", "01", "00", "10"}
    };
    // S2 盒
    public static String[][] S2_box = new String[][]{
            {"00", "01", "10", "11"},
            {"10", "11", "01", "00"},
            {"11", "00", "01", "10"},
            {"10", "01", "00", "11"}
    };
    // SPBox
    public static int[] P4 = new int[]{2, 4, 3, 1};

    // 执行置换操作
    public static String substitute(String str, int[] P) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < P.length; i++) {
            sb.append(str.charAt((P[i]) - 1));
        }
        return new String(sb);
    }

    // 执行异或操作
    public static String xor(String str, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == key.charAt(i)) {
                sb.append("0");
            } else {
                sb.append("1");
            }
        }
        return new String(sb);
    }

    // 在S盒中查找
    public static String searchSbox(String str, int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        sb.append(str.charAt(3));
        String ret = new String(sb);
        StringBuilder sb1 = new StringBuilder();
        sb1.append(str.charAt(1));
        sb1.append(str.charAt(2));
        String ret1 = new String(sb1);
        String retu = new String();
        if (n == 1) {
            retu = S1_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
        } else {
            retu = S2_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
        }
        return retu;
    }

    // 获取key1和key2
    public static void getkey(String mainkey) {
        //System.out.println("-----请输入主密钥(10位)------");
        //Scanner sc = new Scanner(System.in);
        //String mainkey = sc.nextLine();
        mainkey = substitute(mainkey, P10);
        String Ls11 = mainkey.substring(0, 5);
        Ls11 = leftShift(Ls11, 1); // 1位左移
        String Ls12 = mainkey.substring(5, 10);
        Ls12 = leftShift(Ls12, 1); // 1位左移
        key1 = Ls11 + Ls12;
        key1 = substitute(key1, P8);
        //System.out.println("key1= " + key1);
        String Ls21 = leftShift(Ls11, 2); // 2位左移
        String Ls22 = leftShift(Ls12, 2); // 2位左移
        key2 = Ls21 + Ls22;
        key2 = substitute(key2, P8);
        //System.out.println("key2= " + key2);
    }

    //密钥拓展
    public static String leftShift(String str, int n) {//n表示左移的位数
        char[] ch = str.toCharArray();
        char[] copy_ch = new char[5];
        for (int i = 0; i < ch.length; i++) {
            int a = ((i - n) % ch.length);
            if (a < 0) {
                if (n == 1) {
                    copy_ch[ch.length - 1] = ch[i];
                }
                if (n == 2) {
                    if (i == 0) {
                        copy_ch[ch.length - 2] = ch[i];
                    } else {
                        copy_ch[ch.length - 1] = ch[i];
                    }
                }
            } else {
                copy_ch[a] = ch[i];
            }
        }
        return new String(copy_ch);
    }

    // 加密
    public static String encrypt(String plaintext, String key,boolean isChar) {
        getkey(key);
        //IP置换
        plaintext = substitute(plaintext, IP);
        String L0 = plaintext.substring(0, 4);
        String R0 = plaintext.substring(4, 8);

        //f函数开始
        //1.R用EP-box进行拓展置换
        String R0E = substitute(R0, EP);
        //2.再与k1进行异或
        R0E = xor(R0E, key1);
        //3.替换盒S-box
        String S1 = R0E.substring(0, 4);
        String S2 = R0E.substring(4, 8);
        S1 = searchSbox(S1, 1);
        S2 = searchSbox(S2, 2);
        String SS = S1 + S2;
        //4.直接置换（利用P4）
        String f1 = substitute(SS, P4);
        //f函数结束

        //// 计算L1和R1，并左右互换
        String L1 = R0;
        String R1 = xor(f1, L0);


        // 第二轮f函数
        String R11 = substitute(R1, EP);
        R11 = xor(R11, key2);
        S1 = R11.substring(0, 4);
        S2 = R11.substring(4, 8);
        S1 = searchSbox(S1, 1);
        S2 = searchSbox(S2, 2);
        SS = S1 + S2;
        String f2 = substitute(SS, P4);
        String L2 = xor(f2, L1);
        String R2 = R1;

        // 计算L2和R2
        String ciphertext = L2 + R2;
        ciphertext = substitute(ciphertext, IP_1);
        //System.out.println("密文: " + ciphertext);
        if (isChar){
            return String.valueOf((char)Integer.parseInt(ciphertext, 2));
        }else {
            return ciphertext;
        }
    }

    // 解密
    public static String decode(String ciphertext, String key,boolean isChar) {
        getkey(key);
        ciphertext = substitute(ciphertext, IP);
        String L0 = ciphertext.substring(0, 4);
        String R0 = ciphertext.substring(4, 8);
        String R0E = substitute(R0, EP);
        R0E = xor(R0E, key2);
        String S1 = R0E.substring(0, 4);
        String S2 = R0E.substring(4, 8);
        S1 = searchSbox(S1, 1);
        S2 = searchSbox(S2, 2);
        String SS = S1 + S2;
        String f1 = substitute(SS, P4);
        String L1 = R0;
        String R1 = xor(f1, L0);
        // 计算L1和R1

        // 第二轮
        String R11 = substitute(R1, EP);
        R11 = xor(R11, key1);
        S1 = R11.substring(0, 4);
        S2 = R11.substring(4, 8);
        S1 = searchSbox(S1, 1);
        S2 = searchSbox(S2, 2);
        SS = S1 + S2;
        String f2 = substitute(SS, P4);
        String L2 = xor(f2, L1);
        String R2 = R1;
        // 计算L2和R2

        String plaintext = L2 + R2;
        plaintext = substitute(plaintext, IP_1);
        //System.out.println("明文: " + plaintext);

        if (isChar){
            return String.valueOf((char)Integer.parseInt(plaintext, 2));
        }else {
            return plaintext;
        }
    }
}