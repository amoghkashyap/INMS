import *as express from "express";

import { ApiPathPrefix } from "../config/constants";
import INMSController from "../controllers/inms.controller";

export default class INMSRouter {

    public static create(router: express.Router) {

     
       /**
        * 
        * @swagger
        * "/inms/INMS": {
           "get":{
               "tags": [
                   "INMS"
               ],
               "summary": "Get list of ingredients from database",
               "description": "Get list of ingredients from database",
               "produces": [
                   "application/json"
               ],
               "parameters": [ 
                   {
                       "in": "query",
                       "name": "containerId",
                       "description": "container ID",
                       "type": "string"
                   }  
               ],
               "responses": {
                   "200":
                   {
                       "description": "Success"
                   }
               },
               "security": [
                   {
                       "jwt": []
                   }
               ]
           }
        }
   
        
       */
       router.get(ApiPathPrefix.INMS_PATH + "/INMS", INMSController.getIngredients);

       /**
        * 
        * @swagger
        * "/inms/recipes": {
           "post":{
               "tags": [
                   "INMS"
               ],
               "summary": "Get list of recipes from database",
               "description": "Get list of recipes from database",
               "consumes": [
                   "application/json"
               ],
               "produces": [
                   "text/plain",
                   "application/json"
               ],
               "parameters": [ 
                   {
                       "in": "body",
                       "name": "body",
                       "description": "Get Recipes",
                       "required": true,
                       "schema": {
                           "properties": {
                               "container_id": {
                                   "type":"string"
                               },
                               "ingredients": {
                                   "type":"array",
                                   "items": {
                                       "type":"string"
                                   }
                               },
                               "health_labels": {
                                   "type":"array",
                                   "items": {
                                       "type":"string"
                                   }
                               }
                           }
                       }
                   }  
               ],
               "responses": {
                   "200":
                   {
                       "description": "Success"
                   }
               },
               "security": [
                   {
                       "jwt": []
                   }
               ]
           }
        }
   
        
       */
      router.post(ApiPathPrefix.INMS_PATH + "/recipes", INMSController.getRecipes);

      /**
         * 
         * @swagger
         * "/inms/INMS": {
            "put": {
                "tags": [
                    "INMS"
                ],
                "summary": "Update detected ingredients to DB",
                "description": "Details of detected ingredients to be added to DB",
                "consumes": [
                    "application/json"
                ],
                "produces": [
                    "application/json"
                ],
                "parameters": [
                    {
                        "in": "body", 
                        "name": "body",
                        "description": "Update detected ingredients in db",
                        "schema": {
                            "properties": {
                                "container_id": {
                                    "type":"string"
                                },
                                "ingredients": {
                                    "type": "array",
                                    "items": {
                                        "type": "string"
                                    }
                                }
                            }
                        }
                    }  
                ],
                "responses": {
                    "200": {
                        "description": "successful operation"
                    }
                },
                "security": [
                    {
                        "jwt": []
                    }
                ]
            }
        }

        */
        router.put(ApiPathPrefix.INMS_PATH + "/INMS", INMSController.updateDetectedIngredients);

    }
}
