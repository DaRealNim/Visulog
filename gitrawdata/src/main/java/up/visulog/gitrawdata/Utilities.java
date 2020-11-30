public class Utilities{

  public static double hexToDouble(String s){
    String current = s;
    double ret = 0;

    for (int i = 0; i < current.length(); i++){
      switch (current.charAt(i)){
        case '0':
          break;
        case '1':
          ret += Math.pow(16, s.length() - (i + 1));
          break;
        case '2':
          ret += 2*Math.pow(16, s.length() - (i + 1));
          break;
        case '3':
          ret += 3*Math.pow(16, s.length() - (i + 1));
          break;
        case '4':
          ret += 4*Math.pow(16, s.length() - (i + 1));
          break;
        case '5':
          ret += 5*Math.pow(16, s.length() - (i + 1));
          break;
        case '6':
          ret += 6*Math.pow(16, s.length() - (i + 1));
          break;
        case '7':
          ret += 7*Math.pow(16, s.length() - (i + 1));
          break;
        case '8':
          ret += 8*Math.pow(16, s.length() - (i + 1));
          break;
        case '9':
          ret += 9*Math.pow(16, s.length() - (i + 1));
          break;
        case 'a':
          ret += 10*Math.pow(16, s.length() - (i + 1));
          break;
        case 'b':
          ret += 11*Math.pow(16, s.length() - (i + 1));
          break;
        case 'c':
          ret += 12*Math.pow(16, s.length() - (i + 1));
          break;
        case 'd':
          ret += 13*Math.pow(16, s.length() - (i + 1));
          break;
        case 'e':
          ret += 14*Math.pow(16, s.length() - (i + 1));
          break;
        case 'f':
          ret += 15*Math.pow(16, s.length() - (i + 1));
          break;
        /*default :
          System.out.println("Erreur le charactère n'est pas hexadecimal");
          return -1;*/
      }
    }
    return ret;
  }


/*  public static void main(String[] args) {
    String hexTest = "6304c1acdc1cbdeb8315528781896abc72a021b8";
    String hexTest1 = "c0cf37d6b32897677e4b8f04be012e5379a7ab80";
    String hexTest2 = "9aaf6e09cc30909b32c68b4d5bf4ac50f95ecb93";

    System.out.println(hexToDouble(hexTest));
    System.out.println(hexToDouble(hexTest1));
    System.out.println(hexToDouble(hexTest2));

  }*/


}
