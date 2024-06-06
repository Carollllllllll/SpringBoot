package com.hdu.graduationdesign.Controller;

import com.hdu.graduationdesign.Pojo.calculatePojo;
import org.springframework.web.bind.annotation.*;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

@CrossOrigin
@RestController
public class CalculateSimilarityController {

//    private static int editDis(String a, String b) {
//
//        int aLen = a.length();
//        int bLen = b.length();
//
//        if (aLen == 0) return aLen;
//        if (bLen == 0) return bLen;
//
//        int[][] v = new int[aLen + 1][bLen + 1];
//        for (int i = 0; i <= aLen; ++i) {
//            for (int j = 0; j <= bLen; ++j) {
//                if (i == 0) {
//                    v[i][j] = j;
//                } else if (j == 0) {
//                    v[i][j] = i;
//                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
//                    v[i][j] = v[i - 1][j - 1];
//                } else {
//                    v[i][j] = 1 + Math.min(v[i - 1][j - 1], Math.min(v[i][j - 1], v[i - 1][j]));
//                }
//            }
//        }
//        return v[aLen][bLen];
//    }
//
//    @GetMapping("/getCalculateSimilarity")
//    public static float Levenshtein(@RequestParam(value = "text_left", required = false) String a,
//                                    @RequestParam(value = "text_right", required = false) String b) {
//        if (a == null && b == null) {
//            return 1f;
//        }
//        if (a == null || b == null) {
//            return 0F;
//        }
//        int editDistance = editDis(a, b);
//        return (1 - ((float) editDistance / Math.max(a.length(), b.length()))) * 100;
//    }

    @PostMapping("/getCalculateSimilarity")
    public static double computeTxtSimilar(@RequestBody calculatePojo pojo){
        String txtLeft = pojo.getInputLeft();
        String txtRight = pojo.getInputRight();
        //所有文档的总词库
        List<String> totalWordList = new ArrayList<String>();
        //计算文档的词频
        Map<String, Integer> leftWordCountMap = getWordCountMap(txtLeft, totalWordList);
        Map<String, Float> leftWordTfMap = calculateWordTf(leftWordCountMap);

        Map<String, Integer> rightWordCountMap = getWordCountMap(txtRight, totalWordList);
        Map<String, Float> rightWordTfMap = calculateWordTf(rightWordCountMap);


        //获取文档的特征值
        List<Float> leftFeature = getTxtFeature(totalWordList,leftWordTfMap);
        List<Float> rightFeature = getTxtFeature(totalWordList,rightWordTfMap);

        //计算文档对应特征值的平方和的平方根
        float leftVectorSqrt = calculateVectorSqrt(leftWordTfMap);
        float rightVectorSqrt = calculateVectorSqrt(rightWordTfMap);

        //根据余弦定理公式，计算余弦公式中的分子
        float fenzi = getCosValue(leftFeature,rightFeature);

        //根据余弦定理计算两个文档的余弦值
        double cosValue = 0;
        if (fenzi > 0) {
            cosValue = fenzi / (leftVectorSqrt * rightVectorSqrt);
        }
        cosValue = Double.parseDouble(String.format("%.4f",cosValue));
        return cosValue * 100;

    }


    public static  Map<String,Integer> getWordCountMap(String text,List<String> totalWordList){
        Map<String,Integer> wordCountMap = new HashMap<String,Integer>();
        List<Term> words= HanLP.segment(text);
        int count = 0;
        for(Term tm:words){
            //取字数为两个字或两个字以上名词或动名词作为关键词
            if(tm.word.length()>1 && (tm.nature== Nature.n||tm.nature== Nature.vn)){
                count = 1;
                if(wordCountMap.containsKey(tm.word))
                {
                    count = wordCountMap.get(tm.word) + 1;
                    wordCountMap.remove(tm.word);
                }
                wordCountMap.put(tm.word,count);
                if(!totalWordList.contains(tm.word)){
                    totalWordList.add(tm.word);
                }
            }
        }
        return wordCountMap;
    }



    //计算关键词词频
    private static Map<String, Float> calculateWordTf(Map<String, Integer> wordCountMap) {
        Map<String, Float> wordTfMap =new HashMap<String, Float>();
        int totalWordsCount = 0;
        Collection<Integer> cv = wordCountMap.values();
        for (Integer count : cv) {
            totalWordsCount += count;
        }

        wordTfMap = new HashMap<String, Float>();
        Set<String> keys = wordCountMap.keySet();
        for (String key : keys) {
            wordTfMap.put(key, wordCountMap.get(key) / (float) totalWordsCount);
        }
        return wordTfMap;
    }

    //计算文档对应特征值的平方和的平方根
    private static float calculateVectorSqrt(Map<String, Float> wordTfMap) {
        float result = 0;
        Collection<Float> cols =  wordTfMap.values();
        for(Float temp : cols){
            if (temp > 0) {
                result += temp * temp;
            }
        }
        return (float) Math.sqrt(result);
    }

    private static List<Float> getTxtFeature(List<String> totalWordList, Map<String, Float> wordCountMap){
        List<Float> list =new ArrayList<Float>();
        for(String word :totalWordList){
            float tf = 0;
            if(wordCountMap.containsKey(word)){
                tf = wordCountMap.get(word);
            }
            list.add(tf);
        }
        return list;
    }

    private static float getCosValue(List<Float> leftFeature, List<Float> rightFeature) {
        float fenzi = 0;
        float tempX = 0;
        float tempY = 0;
        for (int i = 0; i < leftFeature.size(); i++) {
            tempX = leftFeature.get(i);
            tempY = rightFeature.get(i);
            if (tempX > 0 && tempY > 0) {
                fenzi += tempX * tempY;
            }
        }
        return fenzi;
    }

}
