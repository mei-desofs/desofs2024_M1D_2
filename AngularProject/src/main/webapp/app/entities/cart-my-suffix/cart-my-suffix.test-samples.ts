import { ICartMySuffix, NewCartMySuffix } from './cart-my-suffix.model';

export const sampleWithRequiredData: ICartMySuffix = {
  id: 1035,
};

export const sampleWithPartialData: ICartMySuffix = {
  id: 7744,
};

export const sampleWithFullData: ICartMySuffix = {
  id: 19524,
  total: 21964,
};

export const sampleWithNewData: NewCartMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
