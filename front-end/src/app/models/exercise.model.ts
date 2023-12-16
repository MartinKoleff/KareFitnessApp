export interface Workout{
  name:string;
  description?:string;
  exercises:Exercises[];
}
export interface Exercises {
  name?: string;
  description: string;
  machineType?: string;
  muscleGroup?: string;
  sets?: ExerciseSet[];
}

export interface ExerciseSet {
  reps?: number;
  weight?: number;
}
export interface MuscleGroup{
  name:string;
  exercises:Exercises[];
}
export const biceps: MuscleGroup = {
  name: 'biceps',
  exercises: [
    {
      name: 'Bicep curl',
      description: 'Description for bicep curl exercise.',
      machineType: 'dumbell',
      muscleGroup: 'biceps',
    },
    {
      name: 'Incline dumbell curl',
      description: 'Description for incline dumbell curl exercise.',
      machineType: 'dumbell',
      muscleGroup: 'biceps',
    },
    {
      name: 'Hammer curl',
      description: 'Description for hammer curl exercise.',
      machineType: 'dumbell',
      muscleGroup: 'biceps',
    },
    {
      name: 'Concentration curl',
      description: 'Description for concentration curl exercise.',
      machineType: 'dumbell',
      muscleGroup: 'biceps',
    },
    {
      name: 'Barbell curl',
      description: 'Description for barbell curl exercise.',
      machineType: 'barbell',
      muscleGroup: 'biceps',
    },
  ]
}
export const triceps: MuscleGroup = {
  name: 'triceps',
  exercises: [
    {
      name: 'Diamond push-up',
      description: 'Description for diamond Push',
      machineType: 'bodyweight',
      muscleGroup: 'triceps',
    },
    {
      name: 'Triceps dips',
      description: 'Description for triceps Dips',
      machineType: 'bodyweight',
      muscleGroup: 'triceps',
    },
    {
      name: 'Overhead triceps extensions',
      description: 'Description for overhead triceps extensions',
      machineType: 'dumbell',
      muscleGroup: 'triceps',
    },
    {
      name: 'Rope pushdowns',
      description: 'Description for rope pushdowns',
      machineType: 'machine',
      muscleGroup: 'triceps',
    },
    {
      name: 'Skull crushers',
      description: 'Description for Barbell Curl exercise.',
      machineType: 'barbell',
      muscleGroup: 'triceps',
    },
  ]
}
export const legs: MuscleGroup = {
  name: 'legs',
  exercises: [
    {
      name: 'Squats',
      description: 'Description for squats',
      machineType: 'barbell',
      muscleGroup: 'legs',
    },
    {
      name: 'Romanian deadlift',
      description: 'Description for romanian deadlift',
      machineType: 'barbell',
      muscleGroup: 'legs',
    },
    {
      name: 'Bulgarian split-squats',
      description: 'Description for bulgarian split-squats',
      machineType: 'dumbell',
      muscleGroup: 'legs',
    },
    {
      name: 'Leg press',
      description: 'Description for leg press',
      machineType: 'machine',
      muscleGroup: 'legs',
    },
    {
      name: 'Hack suqat',
      description: 'Description for hack squats.',
      machineType: 'machine',
      muscleGroup: 'legs',
    },
  ]
}
export const back: MuscleGroup = {
  name: 'back',
  exercises: [
    {
      name: 'Pull-ups',
      description: 'Description for pull-ups',
      machineType: 'bodyweight',
      muscleGroup: 'back',
    },
    {
      name: 'Rows',
      description: 'Description for Rows',
      machineType: 'barbell',
      muscleGroup: 'back',
    },
    {
      name: 'Deadlifts',
      description: 'Description for deadlifts',
      machineType: 'barbell',
      muscleGroup: 'back',
    },
    {
      name: 'One-arm dumbell rows',
      description: 'Description for one-arm dumbell row',
      machineType: 'dumbell',
      muscleGroup: 'back',
    },
    {
      name: 'Landmine rows',
      description: 'Description for landmine rows',
      machineType: 'barbell',
      muscleGroup: 'back',
    },
  ]
}
export const shoulders: MuscleGroup = {
  name: 'shoulders',
  exercises: [
    {
      name: 'Overhead press',
      description: 'Description for overhead press',
      machineType: 'barbell',
      muscleGroup: 'shoulders',
    },
    {
      name: 'Arnold press',
      description: 'Description for arnold press',
      machineType: 'dumbbell',
      muscleGroup: 'shoulders',
    },
    {
      name: 'Lateral raises',
      description: 'Description for lateral raises',
      machineType: 'dumbell',
      muscleGroup: 'shoulders',
    },
    {
      name: 'Front raises',
      description: 'Description for front raises',
      machineType: 'dumbell',
      muscleGroup: 'shoulders',
    },
    {
      name: 'Reverse flye',
      description: 'Description for reverse flye',
      machineType: 'machine',
      muscleGroup: 'shoulders',
    },
  ]
}
export const chest: MuscleGroup = {
  name: 'chest',
  exercises: [
    {
      name: 'Bench press',
      description: 'Description for bench press',
      machineType: 'barbell',
      muscleGroup: 'chest',
    },
    {
      name: 'Push-ups',
      description: 'Description for push-ups',
      machineType: 'bodyweight',
      muscleGroup: 'chest',
    },
    {
      name: 'Machine fly',
      description: 'Description for machine fly',
      machineType: 'machine',
      muscleGroup: 'chest',
    },
    {
      name: 'Incline dumbell press',
      description: 'Description for incline dumbell press',
      machineType: 'dumbell',
      muscleGroup: 'chest',
    },
    {
      name: 'Dips',
      description: 'Description for dips',
      machineType: 'bodyweight',
      muscleGroup: 'chest',
    },
  ]
}
// export const cardio: MuscleGroup = {
//   name: 'cardio',
//   exercises: [
//     {
//       name: 'Running',
//       description: 'Description for running',
//       machineType: 'cardio',
//       muscleGroup: 'chest',
//     },
//     {
//       name: 'Jumping jacks',
//       description: 'Description for jumping jacks',
//       machineType: 'cardio',
//     },
//     {
//       name: 'Lunges',
//       description: 'Description for lunges',
//       machineType: 'cardio',
//     },
//     {
//       name: 'Cycling',
//       description: 'Description for cycling',
//       machineType: 'cardio',
//     },
//     {
//       name: 'Squat jumps',
//       description: 'Description for squat jumps',
//       machineType: 'cardio',
//     },
//   ]
// }
export const abs: MuscleGroup = {
  name: 'abs',
  exercises: [
    {
      name: 'Plank',
      description: 'Description for plank',
      machineType: 'bodyweight',
      muscleGroup: 'abs',
    },
    {
      name: 'Bicycle crunch',
      description: 'Description for bicycle crunch',
      machineType: 'bodyweight',
      muscleGroup: 'abs',
    },
    {
      name: 'Crunch',
      description: 'Description for crunch',
      machineType: 'bodyweight',
      muscleGroup: 'abs',
    },
    {
      name: 'Cable crunch',
      description: 'Description for cable crunch',
      machineType: 'machine',
      muscleGroup: 'abs',
    },
    {
      name: 'Leg raises',
      description: 'Description for leg raises',
      machineType: 'bodyweight',
      muscleGroup: 'abs',
    },
  ]
}
