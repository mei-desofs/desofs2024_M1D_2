import { IRoleMySuffix, NewRoleMySuffix } from './role-my-suffix.model';

export const sampleWithRequiredData: IRoleMySuffix = {
  id: 20117,
};

export const sampleWithPartialData: IRoleMySuffix = {
  id: 11129,
};

export const sampleWithFullData: IRoleMySuffix = {
  id: 2157,
  nameRole: 'solidly',
};

export const sampleWithNewData: NewRoleMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
