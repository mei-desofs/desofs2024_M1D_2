export interface IReceiptMySuffix {
  id: number;
  idPayment?: number | null;
  description?: string | null;
}

export type NewReceiptMySuffix = Omit<IReceiptMySuffix, 'id'> & { id: null };
