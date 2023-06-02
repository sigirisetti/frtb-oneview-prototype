'use strict';

export const MAX_SERIES_LENGTH = 100;

//Spring Boot
//export const restApiBase: string = "http://localhost:8090/frtb-oneview";
//export const wsBase: string = "ws://localhost:8090/frtb-oneview";

//Django
export const restApiBase: string = "http://localhost:8000";
export const wsBase: string = "ws://localhost:8000";

export const enableAuth: boolean = false;

/***
 * DRC REST API
 */
export const getDrcCalibSummary: string = restApiBase + "/drc/get_drc_calib_summary";


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
export const getSAMRWorkflows: string = restApiBase + "/services/config/getSAMRWorkflows";
export const getAllSAMRWorkflowInstances: string = restApiBase + "/services/samr/getAllSAMRWorkflowInstances";
export const getSAMRExecResults: string = restApiBase + "/services/samr/getSAMRExecResults";
export const getSamrDashboardData: string = restApiBase + "/services/samr/getSamrDashboardData";
export const samrFileUpload: string = restApiBase + "/services/samr/upload";

export const forwardCcyPairsUrl: string = restApiBase + "/forwardCcyPairs";
