package com.nokia.inms.serviceImpl;

import com.nokia.inms.requests.GetIngredients;
import com.nokia.inms.requests.GetRecipes;
import inms.BackendRequestsGrpc;
import inms.Inms;
import io.grpc.stub.StreamObserver;

public class BackendRequestImpl extends BackendRequestsGrpc.BackendRequestsImplBase {
    @Override
    public void getIngredients(Inms.GetIngredientsRequest request, StreamObserver<Inms.GetIngredientsResponse> responseObserver) {
        GetIngredients getIngredients = new GetIngredients(request);
        responseObserver.onNext(getIngredients.fetchIngredients());
        responseObserver.onCompleted();
    }

    @Override
    public void getRecipes(Inms.GetRecipesRequest request, StreamObserver<Inms.GetRecipesResponse> responseObserver) {
       GetRecipes getRecipes = new GetRecipes(request);
       responseObserver.onNext(getRecipes.getRecipes());
       responseObserver.onCompleted();
    }
}
