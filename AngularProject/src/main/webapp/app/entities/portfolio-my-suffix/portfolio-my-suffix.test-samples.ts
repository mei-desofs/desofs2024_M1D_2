import dayjs from 'dayjs/esm';

import { IPortfolioMySuffix, NewPortfolioMySuffix } from './portfolio-my-suffix.model';

export const sampleWithRequiredData: IPortfolioMySuffix = {
  id: 2319,
};

export const sampleWithPartialData: IPortfolioMySuffix = {
  id: 1196,
  date: dayjs('2024-05-13'),
  name: 'swift murky',
};

export const sampleWithFullData: IPortfolioMySuffix = {
  id: 26943,
  date: dayjs('2024-05-12'),
  name: 'brr vice alert',
};

export const sampleWithNewData: NewPortfolioMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
