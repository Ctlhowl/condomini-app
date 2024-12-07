import { OperationType } from "../enum/operation-type.enum";
import { PaymentMethod } from "../enum/payment-method.enum";
import { OutlayType } from "../enum/outlay-type.enum";
import { TableAppendix } from "./table-appendix";

export interface Outlay {
    id: number;
    operationType: OperationType;
    createdAt: Date;
    amount: number;
    description: string;
    paymentMethod: PaymentMethod;
    outlayType: OutlayType;

    table: TableAppendix;
}