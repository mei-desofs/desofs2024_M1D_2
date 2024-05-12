export interface IReceiptMySuffix {
  id?: number;
  idPayment?: number | null;
  description?: string | null;
}

export const defaultValue: Readonly<IReceiptMySuffix> = {};
