import * as winston from "winston";

const tsFormat = () => (new Date()).toLocaleString();

let transportConsole = new winston.transports.Console({
    colorize: true,
    prettyPrint: true,
    timestamp: tsFormat
});

export default class Logger {
    private static logger: winston.Logger;

    private constructor() {
    }

    static get Instance() {
    
        if (this.logger === null || this.logger === undefined) {
            this.logger = new winston.Logger({
                transports: [
                    // transport,
                    transportConsole
                ]
            });
        }

        return this.logger;
    }
} 
