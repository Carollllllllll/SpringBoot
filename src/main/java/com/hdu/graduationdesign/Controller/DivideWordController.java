package com.hdu.graduationdesign.Controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hdu.graduationdesign.Config.*;
import com.hdu.graduationdesign.Pojo.inputPojo;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 前端请求数据，允许跨域的注解
@CrossOrigin
// 将数据以JSON字符串的格式传入传出
@RestController
public class DivideWordController {

    private static List<UnigramFeature> unigramFeatures;
    private static Map<String, Feature> features;

    public static void init(String option) {
        System.out.println("[INFO] 开始初始化！");
        System.out.println("[INFO] 开始解析特征模板...");
        unigramFeatures = Utils.parseFeatureTemplate(Config.TEMPLATE_FILE);
        System.out.println("[INFO] 特征模板解析完毕！");

        System.out.println("[INFO] 开始加载模型...");
        if (option.equals("选项1")) {
            System.out.println("[INFO] 加载了model！");
            features = Utils.getModelFromFile(Config.MODEL_FILE);
        } else {
            System.out.println("[INFO] 加载了model_all！");
            features = Utils.getModelFromFile(Config.MODEL_FILE_ALL);
        }
        System.out.println("[INFO] 一共加载了 " + ((features.size() - 16) * 4 + 16) + " 个特征！");

        System.out.println("[INFO] 初始化完毕！");
    }

    public static List<UnigramFeature> getUnigramFeatures() {
        return unigramFeatures;
    }

    public static Map<String, Feature> getFeatures() {
        return features;
    }

    @PostMapping("/divideWord")
    public String divideWord(@RequestBody inputPojo pojo) {
        // 结果字符串
        StringBuffer stringBuffer = new StringBuffer();
        // 获取需要操作的字符串信息
        String input = pojo.getInputString();
        String dynasty = pojo.getSelectedDynastyString();
        String algorithm = pojo.getSelectedAlgorithmString();
        // 对前端传入的字符串进行Url解码处理
        try {
            input = URLDecoder.decode(input, "UTF-8");
            input = input.substring(0, input.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 如果是CRF分词
        if (algorithm.equals("选项1")) {
            init(pojo.getSelectedDynastyString());

            List<String> sentenceList = new ArrayList<>();
            if (input != null) {
                input = input.trim();
                for (int i = 0; i < input.length(); i++) {
                    if (!Character.isWhitespace(input.charAt(i)))
                        sentenceList.add(String.valueOf(input.charAt(i)));
                }
                String[] sentence = new String[sentenceList.size()];
                sentenceList.toArray(sentence);
                String[] flags = Predict.predict(sentence, features, unigramFeatures);
                // System.out.print("\t");
                for (int i = 0; i < sentence.length; i++) {
                    assert flags != null;
                    // System.out.print(sentence[i] + "/" + flags[i] + " ");
                }
                // System.out.println();
                // System.out.print("\t");
                for (int i = 0; i < sentence.length; i++) {
                    if (flags[i].equals("B") || flags[i].equals("M")) {
                        if (i + 1 < sentence.length &&
                                (flags[i + 1].equals("M") || flags[i + 1].equals("E")))
                            // System.out.print(sentence[i]);
                            stringBuffer.append(sentence[i]);
                        else
                            stringBuffer.append(sentence[i]).append(" ");
                    } else
                        stringBuffer.append(sentence[i]).append(" ");
                }
            }
            sentenceList.clear();
        }
        // 如果是HanLP分词
        else if (algorithm.equals("选项2")) {
            List<Term> words= HanLP.segment(input);
            for (Term term : words) {
                stringBuffer.append(term.word).append(" ");
            }
        } else {
            init("选项2");

            List<String> sentenceList = new ArrayList<>();
            if (input != null) {
                input = input.trim();
                for (int i = 0; i < input.length(); i++) {
                    if (!Character.isWhitespace(input.charAt(i)))
                        sentenceList.add(String.valueOf(input.charAt(i)));
                }
                String[] sentence = new String[sentenceList.size()];
                sentenceList.toArray(sentence);
                String[] flags = Predict.predict(sentence, features, unigramFeatures);
                // System.out.print("\t");
                for (int i = 0; i < sentence.length; i++) {
                    assert flags != null;
                }
                for (int i = 0; i < sentence.length; i++) {
                    if (flags[i].equals("B") || flags[i].equals("M")) {
                        if (i + 1 < sentence.length &&
                                (flags[i + 1].equals("M") || flags[i + 1].equals("E")))
                            stringBuffer.append(sentence[i]);
                        else
                            stringBuffer.append(sentence[i]).append(" ");
                    } else
                        stringBuffer.append(sentence[i]).append(" ");
                }
            }
            sentenceList.clear();
        }
        return stringBuffer.toString();
    }
}
