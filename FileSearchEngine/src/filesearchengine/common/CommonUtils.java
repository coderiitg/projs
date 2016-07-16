package filesearchengine.common;

import java.math.BigDecimal;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CommonUtils {
    
    //private static final String specialChars[] = {"\"","'",",",".","?","(",")","#","@","$","{","}"};
    //private static final char specialChars[] = {'"','\"',',','.','?','(',')','#','!','@','{','}'});
    //TODO: add special characters
    //"',.!@$&()?{}#;:
    private static final String SPECIALCHARS = "\"',.!@$&()?{}#;:";
    
    public CommonUtils() {
        super();
    }
    
    /**
     * Round to certain number of decimals
     * 
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(Double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    
    /**
     *given a word strip the unnecessary characters and return a normalized token(s)
     * @param word
     * @return
     */
    public static Object getNormalizedTokens(String word){
        if(word != null){
            //connvert the word to upper case
            word = word.toUpperCase();
            
            //get the word as array of characters
            char[] wordChars = word.toCharArray();
            //the refined chars will be stored here
            char[] strippedWordChars = new char[wordChars.length];
            
            int strippedCharCount = 0;
            
            for(char wordChar : wordChars){
                //whether wordChar is in SPECIALCHAR string
                if(SPECIALCHARS.indexOf(wordChar) != -1){
                    continue;
                }
                strippedWordChars[strippedCharCount++] = wordChar;
            }
            if(strippedCharCount > 0){
                String strippedWord = new String(strippedWordChars, 0, strippedCharCount);
                return strippedWord;
            }
        }
        return null;
    }
    
    /**
     *
     * @param values
     * @return
     */
    public static Double getMod(Collection<Float> values){
        Double sumSquares = 0d;
        for(Float val: values){
            sumSquares += val*val;
        }
        
        return Math.sqrt(sumSquares);
    }
    
    /**
     *Get the similarity score as cosine angle between a vector and b vector i.e (a.b)/(|a|*|b|)
     * @param a
     * @param b
     * @return
     */
    public static Float getSimilarityScore(Map<String, Float> a, Map<String, Float> b){
        float dotProduct = 0;
        if(a != null && b != null && !a.isEmpty() && !b.isEmpty()){
            for(String aKey : a.keySet()){
                Float aKeyVal = a.get(aKey);
                Float bKeyVal = b.get(aKey);
                if(bKeyVal == null){
                    //indicates that aKey is not present in b
                    continue;
                }
                dotProduct += aKeyVal*bKeyVal;
            }
            Collection<Float> aValues = a.values();
            Collection<Float> bValues = b.values();
            
            if(dotProduct > 0){
                Double aMod = getMod(aValues);
                Double bMod = getMod(bValues);
                return (float)(dotProduct/(aMod*bMod));
            }
            return 0f;
        }
        return null;
    }

    /**
     *Sort a map based on value in descending order and return the top few values based on fetchTopValues
     * @param <K>
     * @param <V>
     * @param map
     * @param fetchTopValues - the number of top few values to be returned, null if complete map has to be returned
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Integer fetchTopValues) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        
        int i = 0;
        Map<K, V> result = new LinkedHashMap<>();
        
        //Return complete map if fecthTopValues is null
        if(fetchTopValues == null){
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        else{
            for (Map.Entry<K, V> entry : list) {
                if(++i > fetchTopValues)
                    break;
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
