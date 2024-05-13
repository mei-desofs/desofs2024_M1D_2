import { IRoleUserMySuffix, NewRoleUserMySuffix } from './role-user-my-suffix.model';

export const sampleWithRequiredData: IRoleUserMySuffix = {
  id: 2171,
};

export const sampleWithPartialData: IRoleUserMySuffix = {
  id: 18340,
};

export const sampleWithFullData: IRoleUserMySuffix = {
  id: 11325,
};

export const sampleWithNewData: NewRoleUserMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
