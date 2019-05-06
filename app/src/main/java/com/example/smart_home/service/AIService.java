package com.example.smart_home.service;

import ai.api.AIDataService;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.smart_home.untils.Constance.KEY_AI;

public class AIService {
    private final AIRequest aiRequest;
    private final AIDataService aiDataService;
    private static AIService instance;

    private AIService() {
        AIConfiguration configuration = new AIConfiguration(KEY_AI, AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(configuration);
        aiRequest = new AIRequest();
    }

    public static AIService getInstance() {
        if (instance == null) {
            instance = new AIService();
        }
        return instance;
    }

    public Single<AIResponse> query(final String query) {
        aiRequest.setQuery(query);
        return Single.create((SingleOnSubscribe<AIResponse>) emitter ->
                emitter.onSuccess(aiDataService.request(aiRequest))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
