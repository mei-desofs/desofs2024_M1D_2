import dayjs from 'dayjs/esm';

import { IPaymentMySuffix, NewPaymentMySuffix } from './payment-my-suffix.model';

export const sampleWithRequiredData: IPaymentMySuffix = {
  id: 27347,
};

export const sampleWithPartialData: IPaymentMySuffix = {
  id: 18991,
  idCart: 6618,
};

export const sampleWithFullData: IPaymentMySuffix = {
  id: 6925,
  idCart: 10080,
  date: dayjs('2024-05-12'),
};

export const sampleWithNewData: NewPaymentMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
