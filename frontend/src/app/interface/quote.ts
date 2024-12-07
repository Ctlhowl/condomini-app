import { TableAppendix } from "./table-appendix";

export interface Quote{
    id: number;
    totalAmount: number;
    createdAt: Date;

    table: TableAppendix;
}