export enum MachineType{
  barbell,
  dumbell,
  cable,
  machine,
  bodyweight
}
export enum MuscleGroup{
  chest,
  back,
  shoulders,
  biceps,
  triceps,
  legs,
  abs,
  cardio
}
export interface Exercise{
  sets:string;
  reps:string;
  weight:string;
}
