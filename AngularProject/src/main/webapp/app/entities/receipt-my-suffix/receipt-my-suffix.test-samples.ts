import { IReceiptMySuffix, NewReceiptMySuffix } from './receipt-my-suffix.model';

export const sampleWithRequiredData: IReceiptMySuffix = {
  id: 5492,
};

export const sampleWithPartialData: IReceiptMySuffix = {
  id: 16962,
  idPayment: 29475,
};

export const sampleWithFullData: IReceiptMySuffix = {
  id: 26662,
  idPayment: 32082,
  description: 'fatally for daily',
};

export const sampleWithNewData: NewReceiptMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
