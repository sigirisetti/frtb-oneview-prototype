import { Entity } from "./entity";

export class WorkflowConfig {
    id: number;
    name: string;
    process: string;
    cronExpression: string;
    organization: Entity;
    active: boolean;
}