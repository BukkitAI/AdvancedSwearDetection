public class JaroWinkler {
   public static double calc(CharSequence first, CharSequence second)
   {
     double DEFAULT_SCALING_FACTOR = 0.1D;
     
     if ((first == null) || (second == null)) {
       throw new IllegalArgumentException("Strings must not be null");
     }
     
     double jaro = score(first, second);
     int cl = commonPrefixLength(first, second);
     double matchScore = Math.round((jaro + 0.1D * cl * (1.0D - jaro)) * 100.0D) / 100.0D;
     
     return matchScore;
   }
}
