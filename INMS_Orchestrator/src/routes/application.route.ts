import * as express from "express";

import INMSRouter from "./inms.route";

export default class ApplicationRouter {
  
  public static create(router: express.Router) {
    // version 2 APIs
    // INMS
    INMSRouter.create(router);

  }
}