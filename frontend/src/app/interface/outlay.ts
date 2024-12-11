import { TableAppendix } from "./table-appendix";

export interface Outlay {
    id: number;
    operationType: String;
    created_at: String;
    amount: number;
    description: string;
    paymentMethod: String;
    outlayType: String;

    table: TableAppendix;
}