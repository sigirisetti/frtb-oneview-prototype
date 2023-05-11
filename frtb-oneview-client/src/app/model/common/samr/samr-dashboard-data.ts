import { WorkflowExecDetail } from "../workflow-exec-detail";
import { ChartRiskTypeData } from "./chart-risk-type-key-value";

export class SamrDashboardData {
    valueDate: Date;
    workflowExecDetail: WorkflowExecDetail;
    riskClassLevelResults: ChartRiskTypeData[];
}

