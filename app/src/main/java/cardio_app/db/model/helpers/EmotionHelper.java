package cardio_app.db.model.helpers;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cardio_app.R;
import cardio_app.db.model.Emotion;

public class EmotionHelper {

    private static final Map<Emotion, Integer> emotionToButtonMap;
    private static final Map<Integer, Emotion> buttonToEmotionMap;
    private static final Map<Emotion, Integer> emotionToImageMap;
    private static final Map<Emotion, Integer> emotionToDescriptionMap;

    static {
        emotionToButtonMap = new HashMap<Emotion, Integer>();
        emotionToButtonMap.put(Emotion.HAPPY, R.id.button_happy);
        emotionToButtonMap.put(Emotion.SAD, R.id.button_sad);
        emotionToButtonMap.put(Emotion.ANGRY, R.id.button_angry);
        emotionToButtonMap.put(Emotion.CRYING, R.id.button_crying);
        emotionToButtonMap.put(Emotion.STRESSED, R.id.button_stressed);
    }

    static {
        buttonToEmotionMap = new HashMap<Integer, Emotion>();
        buttonToEmotionMap.put(R.id.button_happy, Emotion.HAPPY);
        buttonToEmotionMap.put(R.id.button_sad, Emotion.SAD);
        buttonToEmotionMap.put(R.id.button_angry, Emotion.ANGRY);
        buttonToEmotionMap.put(R.id.button_crying, Emotion.CRYING);
        buttonToEmotionMap.put(R.id.button_stressed, Emotion.STRESSED);
    }

    static {
        emotionToImageMap = new HashMap<Emotion, Integer>();
        emotionToImageMap.put(Emotion.HAPPY, R.drawable.happy_small);
        emotionToImageMap.put(Emotion.SAD, R.drawable.sad_small);
        emotionToImageMap.put(Emotion.ANGRY, R.drawable.angry_small);
        emotionToImageMap.put(Emotion.CRYING, R.drawable.crying_small);
        emotionToImageMap.put(Emotion.STRESSED, R.drawable.stressed_small);
    }

    static {
        emotionToDescriptionMap = new HashMap<Emotion, Integer>();
        emotionToDescriptionMap.put(Emotion.HAPPY, R.string.happy);
        emotionToDescriptionMap.put(Emotion.SAD, R.string.sad);
        emotionToDescriptionMap.put(Emotion.ANGRY, R.string.angry);
        emotionToDescriptionMap.put(Emotion.CRYING, R.string.crying);
        emotionToDescriptionMap.put(Emotion.STRESSED, R.string.stressed);
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

    public static Integer getDescription(Emotion emotion) {
        return emotionToDescriptionMap.get(emotion);
    }

    public static Set<Integer> getButtonIdsKeys() {
        return buttonToEmotionMap.keySet();
    }

    public static Set<Emotion> getEmotionsKeys() {
        return emotionToButtonMap.keySet();
    }
}
