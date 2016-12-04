package cardio_app.db.model;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cardio_app.R;

public class EmotionHelper {

    private static final Map<Emotion, Integer> emotionToButtonMap;
    static {
        emotionToButtonMap = new HashMap<Emotion, Integer>();
        emotionToButtonMap.put(Emotion.HAPPY, R.id.button_happy);
        emotionToButtonMap.put(Emotion.SAD, R.id.button_sad);
        emotionToButtonMap.put(Emotion.ANGRY, R.id.button_angry);
        emotionToButtonMap.put(Emotion.CRYING, R.id.button_crying);
        emotionToButtonMap.put(Emotion.STRESSED, R.id.button_stressed);
    }


    private static final Map<Integer, Emotion> buttonToEmotionMap;
    static {
        buttonToEmotionMap = new HashMap<Integer, Emotion>();
        buttonToEmotionMap.put(R.id.button_happy, Emotion.HAPPY);
        buttonToEmotionMap.put(R.id.button_sad, Emotion.SAD);
        buttonToEmotionMap.put(R.id.button_angry, Emotion.ANGRY);
        buttonToEmotionMap.put(R.id.button_crying, Emotion.CRYING);
        buttonToEmotionMap.put(R.id.button_stressed, Emotion.STRESSED);
    }

    private static final Map<Emotion, Integer> emotionToImageMap;
    static {
        emotionToImageMap = new HashMap<Emotion, Integer>();
        emotionToImageMap.put(Emotion.HAPPY, R.drawable.happy_small);
        emotionToImageMap.put(Emotion.SAD, R.drawable.sad_small);
        emotionToImageMap.put(Emotion.ANGRY, R.drawable.angry_small);
        emotionToImageMap.put(Emotion.CRYING, R.drawable.crying_small);
        emotionToImageMap.put(Emotion.STRESSED, R.drawable.stressed_small);
    }


    public static Emotion getEmotion(Integer buttonId) {
        return buttonToEmotionMap.get(buttonId);
    }

    public static Integer getButtonId(Emotion emotion) {
        return emotionToButtonMap.get(emotion);
    }

    public static Integer getImageId(Emotion emotion) {
        return emotionToImageMap.get(emotion);
    }

    public static Set<Integer> getButtonIdsKeys() {
        return buttonToEmotionMap.keySet();
    }

    public static Set<Emotion> getEmotionsKeys() {
        return emotionToButtonMap.keySet();
    }
}
