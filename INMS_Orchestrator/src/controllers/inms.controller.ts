import * as express from "express";
import { HTTP } from "../config/http.codes";
import INMSService from "../services/inms.service";
import HttpGrpcMap from "../config/http.codes";
import Logger from "../config/logger";
import ResponseHandler from "../utils/app.utils.response.handler";

let grpc = require("grpc");
let jwt = require("jsonwebtoken");

export default class INMSController {

    public static getIngredients(req: express.Request, res: express.Response, next: Function): void {
        let getContainerId: string = req.query.containerId || "";
        let getIngredientsRequest = {
            container_id: getContainerId
        };
        
        Logger.Instance.info("INMS_ORCHESTRATOR: " + "GetIngredients Req");
        INMSService.getInstance().getIngredients(getIngredientsRequest, (err, data) => ResponseHandler.getInstance().sendHttpResponse(err, data, res));
    }

    public static getRecipes(req: express.Request, res: express.Response, next: Function): void {
        let getContainerId: string = req.body.container_id || "";
        let getIngredients: Array<string> = req.body.ingredients || "";
        let getHealthLabels: Array<string> = req.body.health_labels || "";
        let getRecipesRequest = {
            container_id: getContainerId,
            ingredients: getIngredients,
            health_labels: getHealthLabels
        };
        Logger.Instance.info("INMS_ORCHESTRATOR: " + "GetRecipess Req" );
        INMSService.getInstance().getRecipes(getRecipesRequest, (err, data) => ResponseHandler.getInstance().sendHttpResponse(err, data, res));
    }

    public static updateDetectedIngredients(req: express.Request, res: express.Response, next: Function): void {
        let getContainerId: string = req.body.container_id || "";
        let getIngredients: Array<string> = req.body.ingredients || "";
        let getdetectedIngredientsRequest = {
            container_id: getContainerId,
            ingredients: getIngredients
        };
        Logger.Instance.info("INMS_ORCHESTRATOR: " + "UpdateDetectedIngredients Req");
        INMSService.getInstance().updateDetectedIngredients(getdetectedIngredientsRequest, (err, data) => ResponseHandler.getInstance().sendHttpResponse(err, data, res));
    }
   
}
