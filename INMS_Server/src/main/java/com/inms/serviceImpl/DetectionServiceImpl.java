package com.inms.serviceImpl;

import com.inms.requests.UpdateDetectedIngredients;
import inms.DetectionGrpc;
import inms.Inms;
import io.grpc.stub.StreamObserver;

public class DetectionServiceImpl extends DetectionGrpc.DetectionImplBase{
    @Override
    public void updateDetectedIngredients(Inms.DetectedIngredientsRequest request, StreamObserver<Inms.DetectedIngredientsResponse> responseObserver) {
        UpdateDetectedIngredients updateDetectedIngredients = new UpdateDetectedIngredients(request);
        responseObserver.onNext(updateDetectedIngredients.updateIngredients());
        responseObserver.onCompleted();
    }
}
