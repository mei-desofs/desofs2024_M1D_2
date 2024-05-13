import dayjs from 'dayjs/esm';

import { IPhotoMySuffix, NewPhotoMySuffix } from './photo-my-suffix.model';

export const sampleWithRequiredData: IPhotoMySuffix = {
  id: 7757,
};

export const sampleWithPartialData: IPhotoMySuffix = {
  id: 14055,
};

export const sampleWithFullData: IPhotoMySuffix = {
  id: 16047,
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  date: dayjs('2024-05-13'),
  state: 'INACTIVE',
};

export const sampleWithNewData: NewPhotoMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
