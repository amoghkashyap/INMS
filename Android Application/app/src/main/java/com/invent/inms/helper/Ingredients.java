package com.invent.inms.helper;
import java.util.ArrayList;
import java.util.List;

import inms.BackendRequestsGrpc;
import inms.Inms;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Ingredients {
    public static List<String> getIngredientsResponse(){
            ManagedChannel channel;
        List<String> ingredients = new ArrayList<>();
            try {
                Inms.GetIngredientsRequest getIngredientsRequest;
                channel = ManagedChannelBuilder.forAddress(Constants.INMS_HOST, Constants.INMS_PORT).usePlaintext().build();
                BackendRequestsGrpc.BackendRequestsBlockingStub backendRequestsBlockingStub = BackendRequestsGrpc.newBlockingStub(channel);
                getIngredientsRequest = Inms.GetIngredientsRequest.newBuilder().setContainerId("inms").build();
                return backendRequestsBlockingStub.getIngredients(getIngredientsRequest).getIngredientsList();
            }catch (Exception e){
                return ingredients;
            }
    }
}
