'use strict';

export const MAX_SERIES_LENGTH = 100;
export const restApiBase: string = "http://localhost:8090/samr-service";
export const wsBase: string = "ws://localhost:8090/samr-service";

/* Web Sockets */
// Mass Quotes
export const massQuotesUrl: string = wsBase + "/massquotes";
// Samples
export const samplesUrl: string = wsBase + "/name";


/* REST API */

//Common
export const loginUrl: string = restApiBase + "/services/login";
export const entityUrl: string = restApiBase + "/entities";
export const setSelectedEntityUrl: string = restApiBase + "/setSelectedEntity";


//SAMR
export const getAllSAMRWorkflowInstances: string = restApiBase + "/services/samr/getAllSAMRWorkflowInstances";
export const getSAMRExecResults: string = restApiBase + "/services/samr/getSAMRExecResults";

export const forwardCcyPairsUrl: string = restApiBase + "/forwardCcyPairs";
