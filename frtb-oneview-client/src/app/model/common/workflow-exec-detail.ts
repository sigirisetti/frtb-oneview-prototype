import { Entity } from "./entity";
import { WorkflowConfig } from "./workflow-config";

export class WorkflowExecDetail {
    id: string;
    excelDate: number;
    date: Date;
    workflow: WorkflowConfig;
    organization: Entity;
    status: string;
    deleted: string;
}