'use strict';

export const MAX_SERIES_LENGTH = 100;
export const restApiBase: string = "http://localhost:8484/ch-client";
export const wsBase: string = "ws://localhost:8484/ch-client";

/* Web Sockets */
// Mass Quotes
export const massQuotesUrl: string = wsBase + "/massquotes";
// Samples
export const samplesUrl: string = wsBase + "/name";


/* REST API */
export const forwardCcyPairsUrl: string = restApiBase + "/forwardCcyPairs";
export const chConnConfig: string = restApiBase + "/chConnConfig";
export const setSelectedEnv: string = restApiBase + "/setSelectedEnv";
